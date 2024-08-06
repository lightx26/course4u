import { useEffect, useState } from "react";
import "../../../assets/css/leaderboard-homepage.css";
import { getDataLeaderBoard } from "../../../apiService/Score.service";
import { Empty } from "antd";
import { handleAvatarUrl } from "../../../utils/handleAvatarUrl";
import { useNavigate } from "react-router-dom";
import LeaderboardItem from "./LeaderBoardItemHomepage";
type DataResponse = {
  avatarUrl: string;
  email: string;
  fullName: string;
  learningTime: number;
  mine: boolean;
  score: number;
  userId: number;
  username: string;
};
type DataLeaderBoardHomepage = {
  rank: number;
  avatarUrl: string;
  fullname: string;
};
const LeaderBoardHomepage = () => {
  const [dataLeaderBoardHomepage, setDataLeadeBoardHomepage] = useState<
    DataLeaderBoardHomepage[]
  >([]);
  const valueYear = "2024";
  const fetchDataLeaderBoardHomepage = async () => {
    try {
      const result = await getDataLeaderBoard(valueYear);
      if (result && result.leaderboardUserDTOs) {
        const dataTmp: DataLeaderBoardHomepage[] = result.leaderboardUserDTOs
          .slice(0, 5)
          .map((item: DataResponse, index: number) => ({
            rank: index + 1,
            avatarUrl: handleAvatarUrl(item.avatarUrl),
            fullname: item.fullName,
          }));
        setDataLeadeBoardHomepage(dataTmp);
      } else {
        setDataLeadeBoardHomepage([]);
      }
    } catch (error) {
      console.error("Error fetching leaderboard data:", error);
      setDataLeadeBoardHomepage([]);
    }
  };

  useEffect(() => {
    fetchDataLeaderBoardHomepage();
  }, []);
  const navigate = useNavigate();

  //Animation
  const [animationEnded, setAnimationEnded] = useState([
    false,
    false,
    false,
    false,
  ]);

  useEffect(() => {
    const timeouts = dataLeaderBoardHomepage.map((_, index) => {
      if (index > 0 && index < 5) {
        return setTimeout(() => {
          setAnimationEnded((prev) => {
            const newState = [...prev];
            newState[index - 1] = true;
            return newState;
          });
        }, index * 600);
      }
      return null;
    });

    return () => {
      timeouts
        .filter((timeout) => timeout !== null)
        .forEach((timeout) => clearTimeout(timeout));
    };
  }, [dataLeaderBoardHomepage]);
  return (
    <div
      className="w-[25%] h-full mt-[25px] pb-[15px] bg-white rounded-[10px]"
      style={{
        boxShadow: "0px 0px 10px 0px rgba(0,0,0,0.1)",
      }}
    >
      <div className="flex items-center justify-center gap-2 py-3">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 576 512"
          style={{
            width: "25px",
            height: "20px",
          }}
        >
          <path d="M400 0L176 0c-26.5 0-48.1 21.8-47.1 48.2c.2 5.3 .4 10.6 .7 15.8L24 64C10.7 64 0 74.7 0 88c0 92.6 33.5 157 78.5 200.7c44.3 43.1 98.3 64.8 138.1 75.8c23.4 6.5 39.4 26 39.4 45.6c0 20.9-17 37.9-37.9 37.9L192 448c-17.7 0-32 14.3-32 32s14.3 32 32 32l192 0c17.7 0 32-14.3 32-32s-14.3-32-32-32l-26.1 0C337 448 320 431 320 410.1c0-19.6 15.9-39.2 39.4-45.6c39.9-11 93.9-32.7 138.2-75.8C542.5 245 576 180.6 576 88c0-13.3-10.7-24-24-24L446.4 64c.3-5.2 .5-10.4 .7-15.8C448.1 21.8 426.5 0 400 0zM48.9 112l84.4 0c9.1 90.1 29.2 150.3 51.9 190.6c-24.9-11-50.8-26.5-73.2-48.3c-32-31.1-58-76-63-142.3zM464.1 254.3c-22.4 21.8-48.3 37.3-73.2 48.3c22.7-40.3 42.8-100.5 51.9-190.6l84.4 0c-5.1 66.3-31.1 111.2-63 142.3z" />
        </svg>
        <span
          style={{ fontSize: "1.3rem", fontWeight: "500", color: "#2b3648" }}
        >
          Leaderboard
        </span>
      </div>
      <hr />
      {dataLeaderBoardHomepage && dataLeaderBoardHomepage.length === 0 ? (
        <>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
        </>
      ) : (
        <>
          <div className="p-[5px_10px]">
            <div className="flex flex-col items-center py-3 gap-1 w-full rounded-[10px]">
              <div className="w-[70px] h-[70px] relative rounded-full ">
                <img
                  src={dataLeaderBoardHomepage[0].avatarUrl}
                  alt=""
                  className="absolute w-full h-full rounded-full object-cover object-center left-0 right-0"
                />
                <div className="absolute w-[30px] h-[30px] rounded-full bg-[#861fa2] right-[-5%] bottom-[-5%] flex justify-center items-center">
                  <svg
                    width="21"
                    height="21"
                    viewBox="0 0 21 21"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      d="M14.5395 2.70224C15.1003 3.13974 15.3753 3.81807 15.5087 4.32307C15.6053 4.68974 15.3312 5.0414 14.9512 5.0364C14.4295 5.02974 13.7045 4.92724 13.1445 4.48974C12.5837 4.05224 12.3087 3.3739 12.1753 2.8689C12.0778 2.50224 12.352 2.15057 12.732 2.15557C13.2537 2.16224 13.9787 2.26474 14.5395 2.70224Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M18.416 6.16808C18.1368 6.82224 17.5518 7.26224 17.0976 7.51974C16.7676 7.70724 16.3576 7.53224 16.2651 7.16391C16.1385 6.65724 16.0518 5.93057 16.331 5.27724C16.6101 4.62307 17.1951 4.18307 17.6493 3.92557C17.9793 3.73807 18.3893 3.91307 18.4818 4.28141C18.6085 4.78724 18.6951 5.51391 18.416 6.16808Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M15.6052 5.60309C16.2794 5.82893 16.7644 6.37726 17.0586 6.80893C17.2719 7.12309 17.1302 7.54559 16.7702 7.66726C16.2752 7.83476 15.5577 7.97893 14.8844 7.75309C14.2102 7.52726 13.7244 6.97893 13.4311 6.54726C13.2177 6.23309 13.3594 5.81059 13.7186 5.68893C14.2136 5.52143 14.9311 5.37726 15.6052 5.60309Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M19.062 10.5572C18.5603 11.0605 17.8537 11.2522 17.3362 11.3238C16.9603 11.3755 16.6445 11.0605 16.6953 10.6847C16.7653 10.1672 16.9545 9.46051 17.4562 8.95718C17.9578 8.45301 18.6645 8.26218 19.182 8.19051C19.5578 8.13884 19.8737 8.45384 19.8228 8.82968C19.7537 9.34634 19.5645 10.0538 19.062 10.5572Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M16.662 8.99054C17.2045 9.45054 17.4512 10.1397 17.5637 10.6497C17.6453 11.0205 17.357 11.3605 16.9778 11.3397C16.4562 11.3114 15.7362 11.1797 15.1945 10.7197C14.652 10.2597 14.4045 9.57054 14.292 9.06054C14.2103 8.68971 14.4987 8.34971 14.8778 8.37054C15.4003 8.39887 16.1203 8.53054 16.662 8.99054Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M17.7027 14.8214C17.0269 15.0431 16.3102 14.8923 15.8169 14.7214C15.4586 14.5973 15.3194 14.1731 15.5352 13.8606C15.8319 13.4314 16.3219 12.8873 16.9969 12.6664C17.6727 12.4448 18.3894 12.5956 18.8827 12.7664C19.2411 12.8906 19.3802 13.3148 19.1644 13.6264C18.8677 14.0564 18.3786 14.6006 17.7027 14.8214Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M16.2745 12.3363C16.5495 12.9922 16.457 13.7188 16.3262 14.2238C16.2312 14.5913 15.8195 14.763 15.4912 14.573C15.0395 14.3113 14.4578 13.8672 14.1837 13.2113C13.9087 12.5555 14.0012 11.8288 14.1328 11.3238C14.2278 10.9563 14.6395 10.7847 14.9678 10.9747C15.4187 11.2363 15.9995 11.6805 16.2745 12.3363Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M14.4409 17.7223C13.7317 17.6756 13.1226 17.2681 12.7292 16.9256C12.4426 16.6764 12.4726 16.2314 12.7892 16.0223C13.2251 15.7348 13.8826 15.4123 14.5917 15.4598C15.3009 15.5064 15.9101 15.9139 16.3042 16.2564C16.5901 16.5056 16.5609 16.9506 16.2442 17.1598C15.8076 17.4473 15.1501 17.7689 14.4409 17.7223Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M14.0436 14.883C14.0536 15.5939 13.6969 16.233 13.3861 16.653C13.1603 16.958 12.7144 16.9647 12.4811 16.6655C12.1594 16.2539 11.7861 15.6247 11.7761 14.9139C11.7661 14.203 12.1228 13.563 12.4336 13.143C12.6594 12.838 13.1053 12.8314 13.3386 13.1305C13.6603 13.543 14.0344 14.1722 14.0436 14.883Z"
                      fill="#F0EEFD"
                    />
                    <path
                      opacity="0.35"
                      d="M17.7801 9.83804C17.7801 4.76887 14.3409 3.12304 14.1943 3.05554C13.8809 2.91054 13.5134 3.04637 13.3684 3.35804C13.2218 3.6697 13.3568 4.04054 13.6668 4.18804C13.7843 4.24387 16.5301 5.59804 16.5301 9.83721C16.5301 14.633 13.9918 15.2747 11.5384 15.8964C11.1801 15.9872 10.8276 16.0764 10.4884 16.178C10.1501 16.0764 9.79678 15.9864 9.43844 15.8964C6.98511 15.2755 4.44678 14.633 4.44678 9.83721C4.44678 5.59804 7.19261 4.24387 7.30678 4.1897C7.62011 4.04554 7.75678 3.67387 7.61178 3.36054C7.46761 3.04637 7.09344 2.91054 6.78261 3.05554C6.63594 3.12304 3.19678 4.76887 3.19678 9.83804C3.19678 15.328 6.30178 16.378 8.76094 17.013C8.47594 17.2472 8.23261 17.5289 8.05844 17.8914C7.90844 18.2022 8.04011 18.5755 8.35094 18.7255C8.43761 18.768 8.53094 18.7872 8.62094 18.7872C8.85344 18.7872 9.07761 18.6572 9.18511 18.433C9.40011 17.9855 9.87594 17.7105 10.4884 17.4939C11.1009 17.7105 11.5768 17.9855 11.7918 18.433C11.8993 18.6572 12.1226 18.7872 12.3559 18.7872C12.4459 18.7872 12.5393 18.768 12.6259 18.7255C12.9368 18.5755 13.0676 18.2022 12.9184 17.8914C12.7443 17.5297 12.5009 17.248 12.2159 17.013C14.6751 16.378 17.7801 15.3272 17.7801 9.83804Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M8.24538 2.15557C8.62454 2.15057 8.89954 2.50224 8.80204 2.8689C8.66871 3.3739 8.39371 4.05224 7.83288 4.48974C7.27288 4.92724 6.54871 5.02974 6.02621 5.0364C5.64704 5.0414 5.37204 4.69057 5.46871 4.32307C5.60204 3.81807 5.87704 3.13974 6.43788 2.70224C6.99871 2.26474 7.72371 2.16224 8.24538 2.15557Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M2.49597 4.28055C2.58847 3.91222 2.99847 3.73722 3.32847 3.92472C3.78264 4.18305 4.36764 4.62305 4.64681 5.27639C4.92597 5.93055 4.8393 6.65722 4.71181 7.16389C4.62014 7.53222 4.21014 7.70722 3.88014 7.51972C3.42597 7.26139 2.84097 6.82139 2.5618 6.16805C2.2818 5.51389 2.36847 4.78722 2.49597 4.28055Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M7.25753 5.68893C7.6167 5.81059 7.7592 6.23309 7.54503 6.54726C7.2517 6.97893 6.7667 7.52726 6.0917 7.75309C5.41836 7.97893 4.70003 7.83476 4.20586 7.66726C3.8467 7.54643 3.70503 7.12309 3.91836 6.80976C4.2117 6.37726 4.69753 5.82893 5.3717 5.60309C6.04586 5.37726 6.76337 5.52143 7.25753 5.68893Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M1.15334 8.82972C1.10251 8.45306 1.41834 8.13806 1.79501 8.19056C2.31251 8.26222 3.01917 8.45306 3.52084 8.95722C4.02251 9.46056 4.21167 10.1672 4.28167 10.6847C4.33251 11.0606 4.01667 11.3756 3.64084 11.3239C3.12334 11.2522 2.41667 11.0614 1.91501 10.5572C1.41251 10.0539 1.22334 9.34639 1.15334 8.82972Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M6.09843 8.37054C6.4776 8.34971 6.76593 8.68971 6.68427 9.06054C6.57177 9.57054 6.32427 10.2597 5.78177 10.7197C5.2401 11.1797 4.5201 11.3114 3.99843 11.3397C3.61927 11.3605 3.33093 11.0205 3.4126 10.6497C3.5251 10.1397 3.7726 9.45054 4.31427 8.99054C4.85677 8.53054 5.57677 8.39887 6.09843 8.37054Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M1.81275 13.6273C1.59692 13.3148 1.73525 12.8915 2.09442 12.7673C2.58775 12.5965 3.30442 12.4456 3.98025 12.6673C4.65525 12.8881 5.14525 13.4323 5.44192 13.8615C5.65775 14.174 5.51942 14.5973 5.16025 14.7223C4.66692 14.8931 3.95025 15.044 3.27442 14.8223C2.59858 14.6006 2.10942 14.0565 1.81275 13.6273Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M6.01095 10.9747C6.33928 10.7847 6.75095 10.9563 6.84595 11.3238C6.97678 11.8297 7.06928 12.5555 6.79511 13.2113C6.52095 13.8672 5.93928 14.3113 5.48761 14.573C5.15928 14.763 4.74761 14.5913 4.65261 14.2238C4.52178 13.718 4.42928 12.9922 4.70428 12.3363C4.97761 11.6805 5.55845 11.2363 6.01095 10.9747Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M4.73358 17.1598C4.41691 16.9506 4.38691 16.5056 4.67358 16.2564C5.06775 15.9131 5.67608 15.5064 6.38608 15.4598C7.09525 15.4123 7.75275 15.7348 8.18858 16.0223C8.50525 16.2314 8.53525 16.6764 8.24858 16.9256C7.85441 17.2689 7.24608 17.6756 6.53691 17.7223C5.82691 17.7689 5.16941 17.4473 4.73358 17.1598Z"
                      fill="#F0EEFD"
                    />
                    <path
                      d="M7.63855 13.1313C7.87272 12.8322 8.31772 12.838 8.54355 13.1438C8.85355 13.5638 9.21105 14.203 9.20105 14.9147C9.19105 15.6255 8.81772 16.2547 8.49605 16.6663C8.26272 16.9655 7.81689 16.9597 7.59105 16.6538C7.28105 16.2338 6.92355 15.5947 6.93355 14.8838C6.94272 14.1722 7.31689 13.543 7.63855 13.1313Z"
                      fill="#F0EEFD"
                    />
                  </svg>
                </div>
              </div>
              <div
                className="font-semibold text-[1.1rem]  mt-1 w-[250px] text-center"
                style={{
                  display: "-webkit-box",
                  WebkitLineClamp: 1,
                  WebkitBoxOrient: "vertical",
                  textOverflow: "ellipsis",
                  whiteSpace: "normal",
                  wordWrap: "break-word",
                  overflow: "hidden",
                }}
                title={
                  dataLeaderBoardHomepage[0].fullname
                    ? dataLeaderBoardHomepage[0].fullname
                    : "Anonymous"
                }
              >
                {dataLeaderBoardHomepage[0].fullname
                  ? dataLeaderBoardHomepage[0].fullname
                  : "Anonymous"}
              </div>
            </div>
          </div>
          <div className="p-[5px_10px] flex flex-col gap-[15px] w-[100%]">
            {dataLeaderBoardHomepage &&
              dataLeaderBoardHomepage.length > 0 &&
              dataLeaderBoardHomepage.map((item, index) => {
                if (index > 0 && index < 5) {
                  return (
                    <LeaderboardItem
                      key={index}
                      item={item}
                      index={index}
                      animationEnded={animationEnded}
                    />
                  );
                }
                return null;
              })}
          </div>

          <div className="p-[0px_10px] w-full h-[45px] mt-[10px]">
            <button
              className="rounded-[10px] border-2 border-[#9352e8] w-full h-full text-[#9352e8] font-medium btnSeemore"
              onClick={() => {
                navigate("/leaderboard");
              }}
            >
              SEE MORE
            </button>
          </div>
        </>
      )}
    </div>
  );
};
export default LeaderBoardHomepage;
