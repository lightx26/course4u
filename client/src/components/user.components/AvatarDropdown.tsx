import { useState, useRef, useEffect } from "react";
import UserCard from "./UserCard";
import AdminPersonalization from "../admin.components/mainPage.components/AdminPersonalization.tsx";

const AvatarDropdown = ({
  avatarUrl,
  role,
}: {
  avatarUrl: string;
  role: string;
}) => {
  const [showCard, setShowCard] = useState(false);
  const timeoutRef = useRef<null | NodeJS.Timeout>(null);

  const handleMouseEnter = () => {
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
    }
    setShowCard(true);
  };

  const handleMouseLeave = () => {
    timeoutRef.current = setTimeout(() => {
      setShowCard(false);
    }, 200);
  };

  useEffect(() => {
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, []);

  return (
    <div
      className="relative w-[40px] h-[40px] rounded-[50%] group select-none"
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      <img
        src={avatarUrl}
        alt="User Avatar"
        className="absolute left-0 right-0 object-cover object-center w-full h-full cursor-pointer rounded-[50%]"
      />
      {showCard && (
        <div className="absolute right-2 top-10 z-[1000]">
          {role.toUpperCase() === "USER" ? (
            <UserCard avatarUrl={avatarUrl} />
          ) : role.toUpperCase() === "ADMIN" ||
            role.toUpperCase() === "ACCOUNTANT" ? (
            <AdminPersonalization avatarUrl={avatarUrl} />
          ) : null}
        </div>
      )}
    </div>
  );
};

export default AvatarDropdown;
