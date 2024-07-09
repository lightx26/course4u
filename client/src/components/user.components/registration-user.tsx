type RegistrationUserProps = {
    telephone: string;
    email: string;
    fullName: string;
    avatarUrl: string;
};

export const RegistrationUser = ({
    telephone,
    email,
    fullName,
    avatarUrl,
}: RegistrationUserProps) => {
    return (
        <div className='flex gap-3'>
            <img
                src={avatarUrl}
                alt='avatar'
                className='w-[74px] h-[74px] rounded-full'
            />
            <div className='flex flex-col'>
                <h4 className='font-semibold text-base text-muted-foreground'>
                    {fullName}
                </h4>
                <p className='text-muted-foreground'>{email}</p>
                <p className='text-muted-foreground'>{telephone}</p>
            </div>
        </div>
    );
};
