import { format } from "date-fns"
import { Button } from "../ui/button"
import { cn } from "../../utils"
import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover"
import { Calendar } from "../ui/calendar"
import { CalendarIcon } from "lucide-react"

type propTypes = {
    className?: string
    date?: Date | undefined
    setDate: (date: Date) => void
}

export default function DatePicker({ date, setDate }: propTypes) {
    return (
        <Popover>
            <PopoverTrigger asChild>
                <Button
                    variant={"outline"}
                    className={cn(
                        "w-[280px] justify-start text-left font-normal",
                        !date && "text-muted-foreground"
                    )}
                >
                    <CalendarIcon className="w-4 h-4 mr-2" />
                    {date ? format(date, "PPP") : <span>Pick a date</span>}
                </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0">
                <Calendar
                    mode="single"
                    selected={date}
                    //@ts-ignore
                    onSelect={setDate}
                    initialFocus
                />
            </PopoverContent>
        </Popover>
    )
}
