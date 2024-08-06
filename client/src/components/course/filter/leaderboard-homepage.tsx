import avatar from "../../../../public/avatar/Default Avatar.svg";
import "../../../assets/css/leaderboard-homepage.css";
const LeaderBoardHomepage = () => {
  return (
    <div className="w-[25%] h-full mt-[25px] pb-[15px] bg-white rounded-[10px]">
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
          style={{ fontSize: "1.2rem", fontWeight: "500", color: "#2b3648" }}
        >
          Leaderboard
        </span>
      </div>
      <hr />
      <div className="p-[5px_10px]">
        <div className="flex flex-col items-center py-3 gap-1 w-full rounded-[10px]">
          <div className="w-[70px] h-[70px] relative rounded-full ">
            <img
              src={avatar}
              alt=""
              className="absolute w-full h-full rounded-full object-cover object-center left-0 right-0"
            />
            <div className="absolute w-[35px] h-[35px] rounded-full bg-[#f0f0f0] right-[-10%] bottom-[-10%] flex justify-center items-center">
              <svg
                width="30px"
                height="30px"
                viewBox="0 0 64 64"
                xmlns="http://www.w3.org/2000/svg"
              >
                <g id="Flat">
                  <g id="Color">
                    <polygon
                      fill="#212529"
                      points="8.26 3 25.94 33.62 38.06 26.62 24.42 3 8.26 3"
                    />

                    <path
                      d="M38.06,26.62l-7.21-12.5-3.72,6.44a21.53,21.53,0,0,0-7,3l5.8,10Z"
                      fill="#111315"
                    />

                    <polygon
                      fill="#dd051d"
                      points="34.6 28.62 29.4 31.62 12.87 3 19.8 3 34.6 28.62"
                    />

                    <polygon
                      fill="#212529"
                      points="39.58 3 25.94 26.62 38.06 33.62 55.74 3 39.58 3"
                    />

                    <path
                      d="M34.6,28.62l-6.06-10.5-1.42,2.46a21.44,21.44,0,0,0-3.46,1.1l5.74,9.94Z"
                      fill="#a60416"
                    />

                    <path
                      d="M43.86,23.58a21.46,21.46,0,0,0-14.17-3.45l-3.75,6.49,12.12,7Z"
                      fill="#111315"
                    />

                    <polygon
                      fill="#dd051d"
                      points="51.13 3 34.6 31.62 29.4 28.62 44.2 3 51.13 3"
                    />

                    <path
                      d="M34.6,31.62l5.74-9.94a21.41,21.41,0,0,0-6-1.55L29.4,28.62Z"
                      fill="#a60416"
                    />

                    <circle cx="32" cy="41.5" fill="#fccd1d" r="19.5" />

                    <circle cx="32" cy="41.5" fill="#f9a215" r="14.5" />

                    <path
                      d="M34.13,43.63V33H29.88a3.19,3.19,0,0,1-3.19,3.19H25.63v4.25h4.25v3.19a2.13,2.13,0,0,1-2.13,2.12H25.63V50H38.38V45.75H36.25A2.12,2.12,0,0,1,34.13,43.63Z"
                      fill="#fccd1d"
                    />
                  </g>
                </g>
              </svg>
            </div>
          </div>
          <div
            className="font-semibold text-[1.1rem]  mt-1 w-[90%] text-center"
            style={{
              display: "-webkit-box",
              WebkitLineClamp: 1,
              WebkitBoxOrient: "vertical",
              textOverflow: "ellipsis",
              whiteSpace: "normal",
              wordWrap: "break-word",
              overflow: "hidden",
            }}
            title="Nguyen Thuc Hoang"
          >
            Nguyen Thuc Hoang
          </div>
        </div>
      </div>

      <div className="p-[5px_10px] flex flex-col gap-[10px] mt-[5px]">
        <div className="flex rounded-[10px] items-center justify-center gap-[10px] bg-[#f0f0f0] h-[55px] p-[5px]">
          <div className="font-bold text-[1rem] text-[#861fa2]">2ND </div>
          <div className="flex items-center gap-[10px]">
            <div className="w-[40px] h-[40px] relative rounded-full">
              <img
                src={avatar}
                alt=""
                className="absolute w-full h-full rounded-full object-cover object-center left-0 right-0"
              />
            </div>
            <span className="font-medium text-[0.9rem]">Nguyen Thuc Hoang</span>
          </div>
        </div>
        <div className="flex rounded-[10px] items-center justify-center gap-[10px] bg-[#f0f0f0] h-[55px] p-[5px] ">
          <div className="font-bold text-[1rem] text-[#861fa2]">3RD</div>
          <div className="flex items-center gap-[10px]">
            <div className="w-[40px] h-[40px] relative rounded-full">
              <img
                src={avatar}
                alt=""
                className="absolute w-full h-full rounded-full object-cover object-center left-0 right-0"
              />
            </div>
            <span className="font-medium text-[0.9rem]">Nguyen Thuc Hoang</span>
          </div>
        </div>
        <div className="flex rounded-[10px] items-center justify-center gap-[10px] bg-[#f0f0f0] h-[55px] p-[5px]">
          <div className="font-bold text-[1rem] text-[#861fa2]">4TH</div>
          <div className="flex items-center gap-[10px]">
            <div className="w-[40px] h-[40px] relative rounded-full">
              <img
                src={avatar}
                alt=""
                className="absolute w-full h-full rounded-full object-cover object-center left-0 right-0"
              />
            </div>
            <span className="font-medium text-[0.9rem]">Nguyen Thuc Hoang</span>
          </div>
        </div>
        <div className="flex rounded-[10px] items-center justify-center gap-[10px] bg-[#f0f0f0] h-[55px] p-[5px]">
          <div className="font-bold text-[1rem] text-[#861fa2]">5TH</div>
          <div className="flex items-center gap-[10px]">
            <div className="w-[40px] h-[40px] relative rounded-full">
              <img
                src={avatar}
                alt=""
                className="absolute w-full h-full rounded-full object-cover object-center left-0 right-0"
              />
            </div>
            <span className="font-medium text-[0.9rem]">Nguyen Thuc Hoang</span>
          </div>
        </div>
      </div>

      <div className="p-[0px_10px] w-full h-[40px] mt-[10px]">
        <button className="rounded-[10px] border-2 border-[#861fa2] w-full h-full text-[#861fa2] font-medium">
          SEE MORE
        </button>
      </div>
    </div>
  );
};
export default LeaderBoardHomepage;
