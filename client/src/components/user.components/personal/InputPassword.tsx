import { useState, FC, ClipboardEventHandler, InputHTMLAttributes } from "react";
import { Input } from "../../ui/input";
import { EyeIcon, EyeOffIcon } from "lucide-react";
import { cn } from "../../../utils";

interface PasswordInputProps extends InputHTMLAttributes<HTMLInputElement> { }

const PasswordInput: FC<PasswordInputProps> = ({ className, ...props }) => {
  const [showPassword, setShowPassword] = useState(false);

  // Prevent copy event
  const handleCopy: ClipboardEventHandler<HTMLInputElement> = (event) => {
    event.preventDefault();
  };

  return (
    <div className="relative">
      <Input
        type={showPassword ? "text" : "password"}
        {...props}
        autoComplete="off"
        autoCorrect="off"
        autoCapitalize="off"
        className={cn("select-none", className)}
        onCopy={handleCopy}
      />
      <button
        type="button"
        className="absolute inset-y-0 right-0 flex items-center pr-3 leading-5"
        onClick={() => setShowPassword(!showPassword)}
        aria-label={showPassword ? "Hide password" : "Show password"}
      >
        {showPassword ? <EyeOffIcon className="w-5" /> : <EyeIcon className="w-5" />}
      </button>
    </div>
  );
};

export default PasswordInput;
