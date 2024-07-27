import { z } from "zod";
import { userRegistrationSchema } from "../schemas/user-schema";
import { handleAvatarUrl } from "../utils/handleAvatarUrl";

export type Feedback = {
  id: number;
  user?: z.infer<typeof userRegistrationSchema>;
  comment: string;
};
type Props = {
  feedbacks: Feedback[];
};
const FeedbackList = ({ feedbacks }: Props) => {
  const formatText = (text: string) => {
    return text.split("\n").map((line: string, index: number) => (
      <span key={index}>
        {line}
        <br />
      </span>
    ));
  };
  return (
    <div>
      <h4 className="mb-5 text-xl font-semibold">Feedback</h4>
      <div className="flex flex-col gap-6">
        {feedbacks.map((feedback) => (
          <div key={feedback.id} className="flex items-start gap-4">
            <img
              src={handleAvatarUrl(feedback.user?.avatarUrl ?? "")}
              alt="avatar"
              className="rounded-full w-[40px] h-[40px] border-2 border-violet-500"
            />
            <div className="">
              <p className="font-semibold">{feedback.user?.fullName}</p>
              <p className="mt-2 w-full whitespace-normal max-h-[160px] overflow-y-auto border-2 border-collapse  p-2 rounded-lg ">
                {formatText(feedback.comment)}
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default FeedbackList;
