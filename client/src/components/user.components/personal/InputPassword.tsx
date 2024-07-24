import { useState } from 'react';
import { Input } from '../../ui/input';
import { EyeIcon, EyeOffIcon } from 'lucide-react';

export default function PasswordInput({ ...props }) {
    const [showPassword, setShowPassword] = useState(false);

    return (
        <div className="relative">
            <Input
                type={showPassword ? 'text' : 'password'}
                {...props}
                autoComplete='off'
                autoCorrect='off'
                autoCapitalize='off'
            />
            <button
                type="button"
                className="absolute inset-y-0 right-0 flex items-center pr-3 text-sm leading-5"
                onClick={() => setShowPassword(!showPassword)}
                aria-label={showPassword ? 'Hide password' : 'Show password'}
            >
                {showPassword ? <EyeOffIcon /> : <EyeIcon />}
            </button>
        </div>
    );
}