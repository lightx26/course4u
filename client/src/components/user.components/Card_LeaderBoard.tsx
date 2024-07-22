interface IProps {
  fullname: string | null;
  email: string;
  avatarUrl: string;
  ranking: number;
  score: number;
  learningTime: number;
}
const Card_LeaderBoard = (props: IProps) => {
  const { fullname, email, avatarUrl, ranking, score, learningTime } = props;
  const rankingColor =
    ranking === 1
      ? "text-yellow-500"
      : ranking === 2
        ? "text-gray-500"
        : "text-red-900";
  const avatar = avatarUrl?.startsWith("http") || avatarUrl?.startsWith("data")
    ? avatarUrl
    : (avatarUrl != null && avatarUrl != undefined && avatarUrl != "") ? `${import.meta.env.VITE_BACKEND_URL}${avatarUrl}` : (`${import.meta.env.BASE_URL}/avatar/Default Avatar.svg`).replace('//', '/');
  return (
    <>
      <div
        className={`card bg-white w-1/4 rounded-lg p-4 flex flex-col justify-between gap-5 ${ranking === 1 ? "pb-10" : ranking === 2 ? "pb-5" : "pb-2.5"
          }`}
      >
        <div className="flex items-start justify-start gap-5">
          <div className="relative w-[70px] h-[70px] rounded-full">
            <img
              src={avatar}
              alt=""
              className="absolute left-0 right-0 object-cover object-center w-full h-full rounded-full"
            />
            <div
              className={`w-9 h-9 rounded-full absolute bottom-0 right-0 ${ranking === 1
                ? "bg-yellow-500"
                : ranking === 2
                  ? "bg-gray-500"
                  : "bg-red-900"
                } flex items-center justify-center`}
            >
              <svg
                width="22"
                height="18"
                viewBox="0 0 22 18"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M12.9994 2.00109C12.9994 2.53109 12.7934 3.01309 12.4564 3.37109L15.0804 6.65109C15.1161 6.69585 15.166 6.72702 15.2219 6.7394C15.2778 6.75178 15.3362 6.74461 15.3874 6.71909L18.0374 5.39309C17.9802 5.10297 17.9878 4.80377 18.0599 4.51695C18.1319 4.23014 18.2665 3.96282 18.454 3.73417C18.6416 3.50553 18.8774 3.32122 19.1446 3.19448C19.4118 3.06774 19.7037 3.00169 19.9994 3.00109C20.4911 3.00119 20.9655 3.18241 21.3321 3.51012C21.6986 3.83784 21.9316 4.28909 21.9865 4.77769C22.0414 5.26629 21.9144 5.758 21.6298 6.15891C21.3451 6.55983 20.9228 6.84184 20.4434 6.95109L18.6394 16.5741C18.5641 16.9752 18.351 17.3373 18.0369 17.5979C17.7228 17.8585 17.3275 18.0011 16.9194 18.0011H5.07942C4.67131 18.0011 4.27604 17.8585 3.96196 17.5979C3.64788 17.3373 3.43474 16.9752 3.35942 16.5741L1.55542 6.95109C1.17203 6.86368 0.822912 6.66519 0.551721 6.38044C0.280531 6.09569 0.0992981 5.73731 0.0306835 5.35011C-0.0379311 4.96292 0.00911684 4.56409 0.165945 4.20349C0.322773 3.84289 0.582422 3.53652 0.912428 3.32268C1.24243 3.10885 1.62815 2.99705 2.02136 3.00125C2.41456 3.00545 2.79781 3.12546 3.12317 3.34629C3.44854 3.56712 3.70158 3.87897 3.85067 4.24284C3.99976 4.60671 4.03828 5.00645 3.96142 5.39209L6.61142 6.71809C6.66264 6.74361 6.72107 6.75078 6.77695 6.7384C6.83282 6.72602 6.88276 6.69485 6.91842 6.65009L9.54242 3.37009C9.31885 3.13235 9.15686 2.84351 9.07058 2.52877C8.9843 2.21403 8.97635 1.88296 9.04742 1.56445C9.11849 1.24593 9.26642 0.949648 9.47832 0.701443C9.69021 0.453239 9.95962 0.260656 10.263 0.140498C10.5665 0.0203405 10.8947 -0.0237419 11.2191 0.0120971C11.5434 0.0479362 11.8541 0.162608 12.124 0.346107C12.3939 0.529606 12.6147 0.776358 12.7673 1.06483C12.9199 1.35331 12.9996 1.67474 12.9994 2.00109ZM10.9994 14.0011C11.5298 14.0011 12.0386 13.7904 12.4136 13.4153C12.7887 13.0402 12.9994 12.5315 12.9994 12.0011C12.9994 11.4707 12.7887 10.962 12.4136 10.5869C12.0386 10.2118 11.5298 10.0011 10.9994 10.0011C10.469 10.0011 9.96028 10.2118 9.5852 10.5869C9.21013 10.962 8.99942 11.4707 8.99942 12.0011C8.99942 12.5315 9.21013 13.0402 9.5852 13.4153C9.96028 13.7904 10.469 14.0011 10.9994 14.0011Z"
                  fill="white"
                />
              </svg>
            </div>
          </div>
          <div>
            <div className="w-[70%]">
              <div
                  className="text-xl font-medium mb-1 w-[100%] "
                  style={{
                    display: "-webkit-box",
                    WebkitLineClamp: 2,
                    WebkitBoxOrient: "vertical",
                    textOverflow: "ellipsis",
                    whiteSpace: "normal",
                    wordWrap: "break-word",
                    overflow: "hidden",
                  }}
              >
              {fullname ? `${fullname}` : "Anonymous"}
            </div>

            <div
              className="text-sm font-normal w-[100%]"
              style={{
                display: "-webkit-box",
                WebkitLineClamp: 1,
                WebkitBoxOrient: "vertical",
                textOverflow: "ellipsis",
                whiteSpace: "normal",
                wordWrap: "break-word",
                overflow: "hidden",
              }}
            >
              {email}
            </div>
          </div>
        </div>
        <div className="flex items-center justify-between pr-2.5">
          <div>
            <div className={`text-lg font-medium ${rankingColor}`}>Score</div>
            <div className={`text-xl font-medium ${rankingColor}`}>{score}</div>
          </div>
          <div>
            <div className={`text-lg font-medium ${rankingColor}`}>
              Learning time/year
            </div>
            <div className={`text-xl font-medium ${rankingColor}`}>
              {learningTime} day
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default Card_LeaderBoard;
