import { useEffect, useRef, useState } from "react";
import { Button } from "./ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover";

import { CircleAlert, CircleCheck, CircleX, TriangleAlert } from "lucide-react";
import { timeAgo } from "../utils/convertTime";
import {
    getAllNotificationsByCurrUser,
    markAllNotificationsAsRead,
} from "../service/notification";

type Notification = {
    id: number;
    content: string;
    createdAt: string;
    type: string;
    seen: boolean;
};

type IProps = {
    children: React.ReactNode;
    setCountUnread: (count: number) => void;
    countUnread: number;
};

export function Notification(props: IProps) {
    const { children, countUnread, setCountUnread } = props;
    const [isLoading, setIsLoading] = useState(false);
    const [notifications, setNotifications] = useState<Notification[] | []>([]);
    const [buffer, setBuffer] = useState<Notification[] | []>([]);
    // Last notifications from database
    const [isLast, setIsLast] = useState(false);
    // Last notifications from buffer
    const [isExhausted, setIsExhausted] = useState(false);

    const defaultBatchSize = 30;
    const firstLoadNum = 10;
    const defaultLoadNum = 5;

    const [isOpen, setIsOpen] = useState(false);
    const popoverContentRef = useRef<HTMLDivElement>(null); // Ref for PopoverContent

    const markAllAsRead = async () => {
        if (countUnread === 0) {
            return;
        }

        await markAllNotificationsAsRead();
        setNotifications((prev) =>
            prev.map((notification) => ({ ...notification, seen: true }))
        );
        setBuffer((prev) =>
            prev.map((notification) => ({ ...notification, seen: true }))
        );
        setCountUnread(0);
    };

    // Fetch notifications from server to buffer
    const fetchNotifications = async () => {
        let res: any;

        // If there are no notifications in the buffer, fetch the first 10 notifications from server
        if (notifications.length === 0) {
            res = await getAllNotificationsByCurrUser(defaultBatchSize);
            setCountUnread(res.totalUnread);
        }

        // If there are notifications in the buffer, fetch the next 10 notifications from server,
        // starting from the last notification in the buffer
        else {
            const lastNotification = buffer[buffer.length - 1];
            res = await getAllNotificationsByCurrUser(
                defaultBatchSize,
                new Date(lastNotification.createdAt)
            );
            setCountUnread(res.totalUnread);
        }

        if (res.content.length > 0) {
            if (isOpen) {
                setBuffer([...buffer, ...res.content]);
            }
        } else {
            setIsLast(true);
        }
    };

    // Load notifications from buffer to notifications
    const loadNotifications = async (
        buffer: Notification[],
        loadNum: number
    ) => {
        if (loadNum === firstLoadNum) {
            setNotifications(buffer.slice(0, loadNum));
            return;
        }

        setIsLoading(true);
        setTimeout(() => {
            setNotifications((prev) => [
                ...prev,
                ...buffer.slice(prev.length, prev.length + loadNum),
            ]);
            setIsLoading(false);
        }, 1000);
    };

    const handleScroll = () => {
        const element = popoverContentRef.current; // Get PopoverContent element
        if (element != null) {
            if (
                element.scrollHeight - element.scrollTop <=
                element.offsetHeight
            ) {
                loadNotifications(buffer, defaultLoadNum);
            }
        }
    };

    useEffect(() => {
        fetchNotifications();
    }, []);

    useEffect(() => {
        if (isOpen) {
            fetchNotifications();
        }

        return () => {
            setBuffer([]);
            setNotifications([]);
            setIsLoading(false);
            setIsLast(false);
            setIsExhausted(false);
        };
    }, [isOpen]);

    useEffect(() => {
        if (buffer.length === 0) {
            return;
        }

        if (buffer.length - notifications.length <= defaultLoadNum) {
            fetchNotifications();
        }

        if (buffer.length === notifications.length) {
            setIsExhausted(true);
        }
    }, [notifications]);

    useEffect(() => {
        if (buffer.length === 0) {
            return;
        } else if (isOpen && notifications.length === 0) {
            loadNotifications(buffer, firstLoadNum);
        }

        const element = popoverContentRef.current; // Get PopoverContent element

        if (element && (!isLast || !isExhausted)) {
            element.addEventListener("scroll", handleScroll);
        }

        return () => {
            if (element) {
                element.removeEventListener("scroll", handleScroll);
            }
        };
    }, [buffer, isLast, isExhausted, isOpen]);

    return (
        <Popover onOpenChange={setIsOpen}>
            <PopoverTrigger asChild>{children}</PopoverTrigger>
            <PopoverContent
                id='notification-popover'
                ref={popoverContentRef} // Attach ref
                className='w-[420px] max-h-[400px] overflow-y-auto p-0 mt-2'
                onOpenAutoFocus={(e) => e.preventDefault()}
            >
                <div className='flex items-center justify-between p-3'>
                    <h3 className='text-lg font-semibold'>Notifications</h3>
                    <Button variant='link' onClick={markAllAsRead}>
                        Mark all as read
                    </Button>
                </div>
                <div className='mt-0'>
                    {notifications.map((notification) => (
                        <div
                            key={notification.id}
                            className={`flex items-start gap-3 py-2 hover:bg-gray-200 ${
                                !notification.seen ? " bg-gray-100" : ""
                            }`}
                        >
                            <div className='mt-2 ml-2'>
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
                                    <TriangleAlert
                                        width={30}
                                        height={30}
                                        color='orange'
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
                            <div className='flex flex-col flex-grow gap-1'>
                                <div
                                    className={
                                        "h-10 mt-1 pb-1 mr-2 flex items-center"
                                    }
                                    title={notification.content}
                                >
                                    <span
                                        className={
                                            "text-sm line-clamp-2" +
                                            (notification.seen
                                                ? " text-gray-800"
                                                : " text-black font-medium")
                                        }
                                    >
                                        {notification.content}
                                    </span>
                                </div>
                                <div className='mr-2 text-xs text-right text-gray-400'>
                                    {timeAgo(notification.createdAt)}
                                </div>
                            </div>
                        </div>
                    ))}
                    {isLoading && notifications.length > 0 && (
                        <div className='py-2 mt-2 text-center text-gray-300'>
                            Loading...
                        </div>
                    )}
                    {isLast && notifications.length === buffer.length && (
                        <div className='pb-2 mt-2 text-center text-gray-400'>
                            You have reached the end of the notifications.
                        </div>
                    )}
                </div>
            </PopoverContent>
        </Popover>
    );
}
