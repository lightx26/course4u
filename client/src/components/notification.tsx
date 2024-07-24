import { useEffect, useState } from "react";
import { Button } from "./ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover";
import { getAllNotificationsByCurrUser } from "../apiService/Notification.service";
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
    const [isFetched, setIsFetched] = useState(false);
    const [isLast, setIsLast] = useState(false);

    const defaultBatchSize = 20;

    // Fetch notifications from server to buffer
    const fetchNotifications = async () => {
        setIsLoading(true);

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
            console.log("res", res);
        }

        if (res.length > 0) {
            setBuffer([...buffer, ...res])
            setIsFetched(true);
        }

        else {
            console.log("No more notifications");
            setIsLast(true);
        }

        setTimeout(() => {
            setIsLoading(false);
        }, 1000);
    }


    // Load notifications from buffer to notifications
    const loadNotifications = async (buffer: Notification[]) => {
        console.log(notifications.length, buffer.length);
        setNotifications(prev => [...prev, ...buffer.slice(prev.length, prev.length + 5)]);
    }

    const handleScroll = () => {
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
            console.log("Load more");
            loadNotifications(buffer);
        }
    }

    useEffect(() => {
        console.log("First fetch");
        fetchNotifications();
    }, []);

    useEffect(() => {
        console.log("buffer length", buffer.length);
        console.log("notification length", notifications.length);

        if (buffer.length === 0) {
            return;
        }

        if (buffer.length - notifications.length <= 5) {
            fetchNotifications();
        }
    }, [notifications]);

    useEffect(() => {

        if (buffer.length === 0) {
            return;
        }

        else if (notifications.length === 0) {
            console.log("First load");
            loadNotifications(buffer);
        }

        if (!isLast) {
            console.log("Add event listener");
            window.addEventListener("scroll", handleScroll);
        }

        return () => {
            window.removeEventListener("scroll", handleScroll);
        };
    }, [isFetched, isLast]);

    return (
        <Popover>
            <PopoverTrigger asChild>{children}</PopoverTrigger>
            <PopoverContent
                className='w-[400px] max-h-[400px] overflow-y-auto'
                onOpenAutoFocus={(e) => e.preventDefault()}
            >
                <div className='flex items-center justify-between'>
                    <h3 className='text-lg font-semibold'>Notifications</h3>
                    <Button variant='link'>Mark all as read</Button>
                </div>
                <div className='mt-4'>
                    {isLoading ? (
                        <div>Loading...</div>
                    ) : (
                        notifications.map((notification) => (
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
                                    <p className='text-sm line-clamp-2 w-[80%]'>
                                        {notification.content}
                                    </p>
                                    <p className='absolute bottom-0 text-xs text-gray-300 right-2'>
                                        {new Date(notification.createdDate).toLocaleString('en-US', {
                                            month: '2-digit',
                                            day: '2-digit',
                                            year: 'numeric',
                                        })}
                                    </p>
                                </div>
                            </div>
                        ))
                    )}
                    {isLast && (
                        <div className='text-center text-gray-300'>
                            No more notifications
                        </div>
                    )}
                </div>
            </PopoverContent>
        </Popover>
    );
}
