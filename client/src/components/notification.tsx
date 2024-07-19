import { useEffect, useState } from "react";
import { Button } from "./ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover";
import { getAllNotificationsByCurrUser } from "../apiService/Notification.service";
import { CircleAlert, CircleCheck, CircleMinus, CircleX } from "lucide-react";
import { getTimeDifference } from "../utils/convertTime";
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
    useEffect(() => {
        const getAllNotification = async () => {
            setIsLoading(true);
            const res = await getAllNotificationsByCurrUser();
            console.log(res);
            setNotifications(res);
            await setTimeout(() => {
                setIsLoading(false);
            });
        };
        getAllNotification();
    }, []);
    return (
        <Popover>
            <PopoverTrigger asChild>{children}</PopoverTrigger>
            <PopoverContent className='w-[400px]'>
                <div className='flex justify-between items-center'>
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
                                className='flex items-center gap-2'
                            >
                                {notification.type === "SUCCESS" && (
                                    <CircleCheck
                                        width={40}
                                        height={40}
                                        color='green'
                                    />
                                )}
                                {notification.type === "ERROR" && (
                                    <CircleX
                                        width={40}
                                        height={40}
                                        color='red'
                                    />
                                )}
                                {notification.type === "WARNING" && (
                                    <CircleMinus
                                        width={40}
                                        height={40}
                                        color='yellow'
                                    />
                                )}
                                {notification.type === "INFORMATION" && (
                                    <CircleAlert
                                        width={40}
                                        height={40}
                                        color='blue'
                                    />
                                )}
                                <div>
                                    <p className='text-sm'>
                                        {notification.content}
                                    </p>
                                    <p className='text-xs text-gray-400'>
                                        {getTimeDifference(
                                            notification.createdDate
                                        )}
                                    </p>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </PopoverContent>
        </Popover>
    );
}
