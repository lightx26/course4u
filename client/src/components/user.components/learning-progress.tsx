import { ChevronRight, Clock } from "lucide-react";
import { formatDate } from "../../utils/convertTime";
import {
    Accordion,
    AccordionContent,
    AccordionItem,
    AccordionTrigger,
} from "../ui/accordion";

type Props = {
    startDate: string;
    endDate: string;
};
const LearningProgress = ({ startDate, endDate }: Props) => {
    return (
        <div className='w-full'>
            <Accordion type='single' collapsible className='w-full'>
                <AccordionItem value='item-1'>
                    <AccordionTrigger>
                        <h2 className='text-bold text-xl'>Learning Progress</h2>
                    </AccordionTrigger>
                    <AccordionContent>
                        <div className='flex justify-start mt-4 gap-20'>
                            <div className='flex justify-center items-center'>
                                <div className='flex items-center gap-2'>
                                    <h4 className='font-bold text-md'>
                                        Start Date
                                    </h4>
                                    <Clock width={20} height={20} />
                                    <p className='text-purple mr-4 font-semibold'>
                                        {formatDate(startDate)}
                                    </p>
                                </div>
                            </div>
                            <div className='flex items-center w-[20%]'>
                                <div className='border-b-2 border-purple w-full' />
                                <ChevronRight
                                    width={20}
                                    height={20}
                                    className='-ml-3'
                                    color='purple'
                                />
                            </div>
                            <div className='flex justify-center'>
                                <div className='flex items-center gap-2'>
                                    <p className='font-bold text-md ml-4'>
                                        End Date
                                    </p>
                                    <Clock width={20} height={20} />
                                    <p className='text-purple font-semibold'>
                                        {endDate
                                            ? formatDate(endDate)
                                            : "Unknown"}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </AccordionContent>
                </AccordionItem>
            </Accordion>
        </div>
    );
};

export default LearningProgress;
