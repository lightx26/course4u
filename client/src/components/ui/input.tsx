import * as React from "react";

import { cn } from "../../utils/utils";

export interface InputProps
    extends React.InputHTMLAttributes<HTMLInputElement> {
    maxValue?: number;
}

const Input = React.forwardRef<HTMLInputElement, InputProps>(
    ({ className, type, maxValue, value, ...props }, ref) => {
        return (
            <div className='relative'>
                <input
                    type={type}
                    className={cn(
                        "flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-black disabled:cursor-not-allowed disabled:opacity-50",
                        className
                    )}
                    ref={ref}
                    value={
                        // @ts-ignore
                        maxValue && value?.length >= maxValue!
                            ? // @ts-ignore
                            value!.slice(0, maxValue - 1)
                            : value
                    }
                    {...props}
                />
                {maxValue && (
                    <span className='absolute text-sm right-3 bottom-3 text-muted-foreground'>
                        {
                            // @ts-ignore
                            value!.length
                        }
                        /{maxValue}
                    </span>
                )}
            </div>
        );
    }
);
Input.displayName = "Input";

export { Input };
