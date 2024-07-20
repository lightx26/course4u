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
            <PopoverContent
                className='w-[400px]'
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
