type DataLeaderBoardHomepage = {
  rank: number;
  avatarUrl: string;
  fullname: string;
};
interface IProps {
  item: DataLeaderBoardHomepage;
  index: number;
  animationEnded: boolean[];
}
const LeaderboardItem = (props: IProps) => {
  const { item, index, animationEnded } = props;
  let backgroundColor;
  let boxShadow;
  switch (index) {
    case 1:
      backgroundColor = "#eec095"; // 2ND
      boxShadow = "0px 0px 5px 0px rgba(0,0,0,0.2)";
      break;
    case 2:
      backgroundColor = "#dfe3e6"; // 3RD
      boxShadow = "0px 0px 5px 0px rgba(0,0,0,0.2)";
      break;
    case 3:
      backgroundColor = "#f6f7f8"; // 4TH
      boxShadow = "none";
      break;
    case 4:
      backgroundColor = "#f6f7f8"; // 5TH
      boxShadow = "none";
      break;
    default:
      backgroundColor = "#ffffff";
      boxShadow = "none";
  }

  if (!animationEnded[index - 1]) return null;

  return (
    <div
      key={index}
      className={`flex rounded-[10px] items-center justify-evenly h-[55px] py-[5px] animation delay-${index}`}
      style={{
        boxShadow: boxShadow,
        backgroundColor: backgroundColor,
      }}
    >
      <div className="font-bold text-[1rem] text-[#a16ce5] w-[20%] flex justify-center">
        {item.rank === 2
          ? "2ND"
          : item.rank === 3
          ? "3RD"
          : item.rank === 4
          ? "4TH"
          : item.rank === 5
          ? "5TH"
          : ""}
      </div>
      <div className="flex items-center gap-[10px] w-[80%]">
        <div className="w-[40px] h-[40px] relative rounded-full">
          <img
            src={item.avatarUrl}
            alt=""
            className="absolute w-full h-full rounded-full object-cover object-center left-0 right-0"
          />
        </div>
        <div
          className="font-medium text-[16px] w-[160px]"
          style={{
            display: "-webkit-box",
            WebkitLineClamp: 1,
            WebkitBoxOrient: "vertical",
            textOverflow: "ellipsis",
            whiteSpace: "normal",
            wordWrap: "break-word",
            overflow: "hidden",
          }}
          title={item.fullname ? item.fullname : "Anonymous"}
        >
          {item.fullname ? item.fullname : "Anonymous"}
        </div>
      </div>
    </div>
  );
};

export default LeaderboardItem;
