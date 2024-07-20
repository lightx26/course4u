import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function buttonVariants({ variant }: { variant: string }) {
  switch (variant) {
    case 'outline':
      return 'border border-gray-300';
    case 'ghost':
      return 'bg-transparent hover:bg-gray-100';
    default:
      return '';
  }
}
