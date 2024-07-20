import * as React from "react"
import { DayPicker } from "react-day-picker"
import { cn } from "../../utils"
import { buttonVariants } from "./button"

export type CalendarProps = React.ComponentProps<typeof DayPicker>

function Calendar({
  className,
  classNames,
  showOutsideDays = true,
  ...props
}: CalendarProps) {
  return (
    <DayPicker
      showOutsideDays={showOutsideDays}
      className={cn("p-3", className)}
      classNames={{
        months: "flex flex-col sm:flex-row space-y-4 sm:space-x-4 sm:space-y-0",
        month: "space-y-4",
        caption: "flex justify-center pt-1 relative items-center",
        caption_label: "text-sm font-medium",
        nav: "space-x-1 flex items-center",
        nav_button: cn(
          buttonVariants({ variant: "outline" }),
          "h-7 w-7 bg-transparent p-0 opacity-0 hover:opacity-0"
        ),
        nav_button_previous: "absolute left-1",
        nav_button_next: "absolute right-1",
        table: "w-full border-collapse space-y-1",
        head_row: "flex",
        head_cell:
          "text-muted-foreground rounded-md w-9 font-normal text-[0.8rem]",
        row: "flex w-full mt-2",
        cell: "h-9 w-9 text-center text-sm p-0 relative [&:has([aria-selected].day-range-end)]:rounded-r-md [&:has([aria-selected].day-outside)]:bg-accent/50 [&:has([aria-selected])]:bg-accent first:[&:has([aria-selected])]:rounded-l-md last:[&:has([aria-selected])]:rounded-r-md focus-within:relative focus-within:z-20",
        day: cn(
          buttonVariants({ variant: "ghost" }),
          "h-9 w-9 p-0 font-normal aria-selected:opacity-100"
        ),
        day_range_end: "day-range-end",
        day_selected:
          "bg-primary text-primary-foreground hover:bg-primary hover:text-primary-foreground focus:bg-primary focus:text-primary-foreground",
        day_today: "bg-accent text-accent-foreground",
        day_outside:
          "day-outside text-muted-foreground opacity-50 aria-selected:bg-accent/50 aria-selected:text-muted-foreground aria-selected:opacity-30",
        day_disabled: "text-muted-foreground opacity-50",
        day_range_middle:
          "aria-selected:bg-accent aria-selected:text-accent-foreground",
        day_hidden: "invisible",
        ...classNames,
      }}
      // components={{
      //   IconLeft: () => <ChevronLeft className="w-4 h-4" />,
      //   IconRight: () => <ChevronRight className="w-4 h-4" />,
      // }}
      {...props}
    />
  )
}
Calendar.displayName = "Calendar"

export { Calendar }

export function CustomCalendar({ ...props }: CalendarProps) {
  const [selectedMonth, setSelectedMonth] = React.useState<number>(new Date().getMonth());
  const [selectedYear, setSelectedYear] = React.useState<number>(new Date().getFullYear());

  const handleMonthChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedMonth(parseInt(e.target.value, 10));
  };

  const handleYearChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedYear(parseInt(e.target.value, 10));
  };

  const months = Array.from({ length: 12 }, (_, i) => new Date(0, i).toLocaleString('en-US', { month: 'long' }));
  const years = Array.from({ length: new Date().getFullYear() - 1900 + 1 }, (_, i) => 1900 + i);

  return (
    <div className="relative p-4 bg-white border rounded-md">
      <div className="flex items-center justify-center mb-4 space-x-2">
        <select
          value={selectedMonth}
          onChange={handleMonthChange}
          className="p-2 text-sm text-gray-700 bg-white border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          {months.map((month, index) => (
            <option key={index} value={index}>
              {month}
            </option>
          ))}
        </select>
        <select
          value={selectedYear}
          onChange={handleYearChange}
          className="p-2 text-sm text-gray-700 bg-white border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          {years.map((year, index) => (
            <option key={index} value={year}>
              {year}
            </option>
          ))}
        </select>
      </div>
      <Calendar {...props} month={new Date(selectedYear, selectedMonth)} />
    </div>
  );
}