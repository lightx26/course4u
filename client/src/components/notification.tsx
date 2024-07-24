import { useEffect, useRef, useState } from "react";
import { Button } from "./ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover";
import { getAllNotificationsByCurrUser, markAllNotificationsAsRead } from "../apiService/Notification.service";
import { CircleAlert, CircleCheck, CircleMinus, CircleX } from "lucide-react";

type Notification = {
    id: number;
    content: string;
    createdDate: string;
    type: string;
    seen: boolean;
};
export function Notification({ children }: { children: React.ReactNode }) {
    const [isLoading, setIsLoading] = useState(false);
    const [notifications, setNotifications] = useState<Notification[] | []>([]);
    const [buffer, setBuffer] = useState<Notification[] | []>([]);
    const [isLast, setIsLast] = useState(false);

    const defaultBatchSize = 30;
    const firstLoadNum = 10;
    const defaultLoadNum = 5;

    const [isOpen, setIsOpen] = useState(false);
    const popoverContentRef = useRef<HTMLDivElement>(null); // Ref for PopoverContent

    const markAllAsRead = async () => {
        await markAllNotificationsAsRead();
        setNotifications((prev) =>
            prev.map((notification) => ({ ...notification, seen: true })))
    }

    // Fetch notifications from server to buffer
    const fetchNotifications = async () => {
        let res: Notification[] = [];

        // If there are no notifications in the buffer, fetch the first 10 notifications from server
        if (notifications.length === 0) {
            res = await getAllNotificationsByCurrUser(defaultBatchSize);
        }

        // If there are notifications in the buffer, fetch the next 10 notifications from server, 
        // starting from the last notification in the buffer
        else {
            const lastNotification = buffer[buffer.length - 1];
            res = await getAllNotificationsByCurrUser(defaultBatchSize, new Date(lastNotification.createdDate));
        }

        if (res.length > 0) {
            setBuffer([...buffer, ...res])
        }

        else {
            console.log("No more notifications");
            setIsLast(true);
        }
    }


    // Load notifications from buffer to notifications
    const loadNotifications = async (buffer: Notification[], loadNum: number) => {
        setIsLoading(true);
        setTimeout(() => {
            setNotifications(prev => [...prev, ...buffer.slice(prev.length, prev.length + loadNum)]);
            setIsLoading(false);
        }, 1000);
    }

    const handleScroll = () => {
        const element = popoverContentRef.current; // Get PopoverContent element
        if (element != null) {
            if (element.scrollHeight - element.scrollTop <= element.offsetHeight) {
                loadNotifications(buffer, defaultLoadNum);
            }
        }
    }

    useEffect(() => {
        fetchNotifications();
    }, []);

    useEffect(() => {
        if (buffer.length === 0) {
            return;
        }

        if (buffer.length - notifications.length <= defaultLoadNum) {
            fetchNotifications();
        }
    }, [notifications]);

    useEffect(() => {

        if (buffer.length === 0) {
            return;
        }

        else if (notifications.length === 0) {
            loadNotifications(buffer, firstLoadNum);
        }

        const element = popoverContentRef.current; // Get PopoverContent element

        console.log(element);

        if (!isLast && element) {
            console.log("Add event listener");
            element.addEventListener("scroll", handleScroll);
        }

        return () => {
            if (element) {
                console.log("Remove event listener");
                element.removeEventListener("scroll", handleScroll);
            }
        };
    }, [buffer, isLast, isOpen]);

    return (
        <Popover onOpenChange={setIsOpen}>
            <PopoverTrigger asChild>{children}</PopoverTrigger>
            <PopoverContent id="notification-popover"
                ref={popoverContentRef} // Attach ref
                className='w-[400px] max-h-[400px] overflow-y-auto'
                onOpenAutoFocus={(e) => e.preventDefault()}
            >
                <div className='flex items-center justify-between'>
                    <h3 className='text-lg font-semibold'>Notifications</h3>
                    <Button variant='link' onClick={markAllAsRead}>Mark all as read</Button>
                </div>
                <div className='mt-4'>

                    {notifications.map((notification) => (
                        <div
                            key={notification.id}
                            className='flex items-center gap-2 h-[50px]'
                        >
                            <div>
                                {notification.type === "SUCCESS" && (
                                    <CircleCheck
                                        width={30}
                                        height={30}
                                        color='green'
                                    />
                                )}
                                {notification.type === "ERROR" && (
                                    <CircleX
                                        width={30}
                                        height={30}
                                        color='red'
                                    />
                                )}
                                {notification.type === "WARNING" && (
                                    <CircleMinus
                                        width={30}
                                        height={30}
                                        color='yellow'
                                    />
                                )}
                                {notification.type === "INFORMATION" && (
                                    <CircleAlert
                                        width={30}
                                        height={30}
                                        color='blue'
                                    />
                                )}
                            </div>
                            <div className='relative'>
                                <p className={`text-sm line-clamp-2 w-[75%] ${!notification.seen ? 'font-bold' : ''}`}
                                    title={notification.content}>
                                    {notification.content}
                                </p>
                                <p className='absolute bottom-0 text-xs w-[20%] text-gray-300 right-1'>
                                    {new Date(notification.createdDate).toLocaleString('en-US', {
                                        month: '2-digit',
                                        day: '2-digit',
                                        year: 'numeric',
                                        hour: '2-digit',
                                        minute: '2-digit',
                                    })}
                                </p>
                            </div>
                        </div>
                    ))
                    }
                    {isLoading && notifications.length && (
                        <div className='text-center text-gray-300 mt-2'>
                            Loading more notifications...
                        </div>
                    )}
                    {isLast && (
                        <div className='text-center text-gray-300 mt-2'>
                            You have reached the end of the notifications.
                        </div>
                    )}
                </div>
            </PopoverContent>
        </Popover>
    );
}
