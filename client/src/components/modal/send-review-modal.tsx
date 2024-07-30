import Rating from "../dynamicComponent/rating";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "../ui/alert-dialog";
import { Button } from "../ui/button";

type Props = {
    children: React.ReactNode;
    handleConfirm: () => void;
    handleCancel?: () => void;
    title: string;
    rating: number;
    setRating: (value: number) => void;
    reviewContent: string;
    setReviewContent: (value: string) => void;
};

export default function SendReviewModal({
    children,
    handleConfirm,
    handleCancel,
    rating,
    setRating,
    reviewContent,
    setReviewContent,
}: Props) {
    const handleClickCancel = () => {
        if (handleCancel)
            handleCancel();
        setReviewContent('');
        setRating(0);
    }

    const handleClickAction = () => {
        handleConfirm();
        setReviewContent('');
        setRating(0);
    }

    return (
        <AlertDialog>
            <AlertDialogTrigger asChild>{children}</AlertDialogTrigger>
            <AlertDialogContent>
                <AlertDialogTitle className='w-full pb-4 font-semibold text-center border-b border-gray-300 border-solid'>
                    Write a review
                </AlertDialogTitle>
                <div>
                    <Rating value={rating} onChange={setRating} />
                </div>
                <div>
                    <label htmlFor='review-text-area'>Review</label>
                    <textarea
                        id='review-text-area'
                        className='w-full h-32 p-2 mt-2 border border-gray-300 rounded-md resize-none focus:border-gray-500'
                        placeholder='Write your review here'
                        maxLength={1000}
                        value={reviewContent}
                        onChange={(e) => setReviewContent(e.target.value)}
                    />
                    <div className='text-sm text-right text-gray-500'>
                        {reviewContent.length}/1000
                    </div>
                </div>

                <div className='flex justify-between gap-2 mt-4'>
                    <AlertDialogCancel asChild>
                        <Button
                            className='w-32 hover:bg-gray-300 text-[16px]'
                            onClick={handleClickCancel}
                            type='button'
                            variant='secondary'
                        >
                            Cancel
                        </Button>
                    </AlertDialogCancel>
                    <AlertDialogAction asChild>
                        <Button
                            type='button'
                            className='w-32 text-[16px] text-white bg-purple h-9'
                            onClick={handleClickAction}
                        >
                            Submit review
                        </Button>
                    </AlertDialogAction>
                </div>
            </AlertDialogContent>
        </AlertDialog>
    );
}
