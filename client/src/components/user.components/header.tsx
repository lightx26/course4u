import React, { useState } from "react";
import imageLogo from "../../assets/images/logo_c4u.svg";
const HeaderHomepage: React.FC = () => {
  const [isDropdownAvatarOpen, setIsDropdownAvatarOpen] = useState(false);

  const toggleDropdown = () => {
    setIsDropdownAvatarOpen(!isDropdownAvatarOpen);
  };

  return (
    <header
      className="bg-white p-4"
      style={{ boxShadow: "rgba(99, 99, 99, 0.2) 0px 2px 4px 0px" }}
    >
      <div className="container mx-auto max-w-screen-xl flex justify-between items-center">
        <div className="flex items-center cursor-pointer">
          <img src={imageLogo} alt="Logo" className="h-10" />
          <span className="text-black text-2xl font-semibold">ourse4U</span>
        </div>

        <div className="flex-grow mx-4 px-16 flex items-center relative">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="2rem"
            height="2rem"
            viewBox="0 0 24 24"
            fill="none"
            style={{
              position: "absolute",
              zIndex: 10,
              paddingLeft: "10px",
            }}
          >
            <path
              d="M10.875 18.75C15.2242 18.75 18.75 15.2242 18.75 10.875C18.75 6.52576 15.2242 3 10.875 3C6.52576 3 3 6.52576 3 10.875C3 15.2242 6.52576 18.75 10.875 18.75Z"
              stroke="#1D2026"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
            <path
              d="M16.4431 16.4434L20.9994 20.9997"
              stroke="#1D2026"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
          <input
            type="text"
            placeholder="Explore courses..."
            className="w-full p-2 px-10 border border-gray-300 rounded-md hover:border-#cccccc-500 focus:border-blue-500 focus:shadow-lg focus:outline-none"
            style={{
              color: "black",
              position: "relative",
              outline: "none",
              backgroundColor: "#f5f7fa",
            }}
            autoFocus
          ></input>
        </div>

        <div className="flex items-center space-x-4">
          <div className="flex items-center justify-center gap-5">
            <button
              className="w-44 h-11 border border-gray-300 rounded-lg bg-violet-700 text-white font-semibold"
              style={{ backgroundColor: "purple" }}
            >
              Create a registration
            </button>
            <div className="flex items-center gap-6">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="2rem"
                height="1.8rem"
                viewBox="0 0 55 41"
                fill="none"
                style={{ cursor: "pointer" }}
              >
                <path
                  d="M23.9273 14.5666C23.8511 14.3345 23.8207 14.0893 23.8378 13.8451C23.8548 13.601 23.919 13.3627 24.0267 13.1437C24.1344 12.9248 24.2834 12.7296 24.4653 12.5693C24.6473 12.4089 24.8584 12.2865 25.0869 12.2091L27.8369 11.2773C28.1125 11.1839 28.406 11.1585 28.6932 11.203C28.9803 11.2475 29.253 11.3608 29.4887 11.5335C29.7243 11.7062 29.9163 11.9333 30.0486 12.1962C30.181 12.4591 30.25 12.7501 30.25 13.0455V22.3636C30.25 22.8579 30.0568 23.3319 29.713 23.6814C29.3692 24.0309 28.9029 24.2273 28.4167 24.2273C27.9304 24.2273 27.4641 24.0309 27.1203 23.6814C26.7765 23.3319 26.5833 22.8579 26.5833 22.3636V15.6312L26.2465 15.7454C26.018 15.8228 25.7769 15.8537 25.5367 15.8364C25.2965 15.819 25.0621 15.7538 24.8467 15.6443C24.6313 15.5349 24.4393 15.3834 24.2815 15.1984C24.1238 15.0135 24.0034 14.7989 23.9273 14.5666ZM55 39.1364C55 39.6306 54.8068 40.1047 54.463 40.4542C54.1192 40.8037 53.6529 41 53.1667 41H1.83333C1.3471 41 0.880787 40.8037 0.536971 40.4542C0.193154 40.1047 0 39.6306 0 39.1364C0 38.6421 0.193154 38.1681 0.536971 37.8186C0.880787 37.4691 1.3471 37.2727 1.83333 37.2727H3.66667V14.9091C3.66667 13.9206 4.05297 12.9725 4.74061 12.2735C5.42824 11.5745 6.36087 11.1818 7.33333 11.1818H16.5V3.72727C16.5 2.73874 16.8863 1.79069 17.5739 1.09169C18.2616 0.392694 19.1942 0 20.1667 0H34.8333C35.8058 0 36.7384 0.392694 37.4261 1.09169C38.1137 1.79069 38.5 2.73874 38.5 3.72727V20.5H47.6667C48.6391 20.5 49.5718 20.8927 50.2594 21.5917C50.947 22.2907 51.3333 23.2387 51.3333 24.2273V37.2727H53.1667C53.6529 37.2727 54.1192 37.4691 54.463 37.8186C54.8068 38.1681 55 38.6421 55 39.1364ZM38.5 24.2273V37.2727H47.6667V24.2273H38.5ZM20.1667 37.2727H34.8333V3.72727H20.1667V37.2727ZM7.33333 37.2727H16.5V14.9091H7.33333V37.2727Z"
                  fill="black"
                />
              </svg>
              <div
                style={{
                  position: "relative",
                }}
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="1.8rem"
                  height="1.8rem"
                  viewBox="0 0 34 40"
                  fill="none"
                  style={{
                    cursor: "pointer",
                  }}
                >
                  <path
                    d="M19.5089 35.583C19.5089 35.9223 19.444 36.2582 19.3179 36.5716C19.1918 36.885 19.007 37.1698 18.774 37.4097C18.5411 37.6496 18.2645 37.8399 17.9601 37.9697C17.6557 38.0995 17.3295 38.1663 17 38.1663"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M14.4911 35.583C14.4911 35.9223 14.556 36.2582 14.6821 36.5716C14.8082 36.885 14.993 37.1698 15.226 37.4097C15.4589 37.6496 15.7355 37.8399 16.0399 37.9697C16.3443 38.0995 16.6705 38.1663 17 38.1663"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M14.4912 33V35.5833"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M19.5089 33L19.5089 35.5833"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M32.0531 30.417C27.0354 33.0003 19.5089 33.0003 17 33.0003"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M32.0532 30.4171C32.0532 25.2505 27.0355 27.8338 27.0355 14.9171C27.0355 9.14061 22.0178 4.58398 19.5089 4.58398"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M1.9469 30.417C6.9646 33.0003 14.4912 33.0003 17 33.0003"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M1.9469 30.4161C1.9469 25.2495 6.9646 27.8328 6.9646 14.9161C6.9646 9.13964 11.9823 4.58301 14.4912 4.58301"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M14.4911 4.58301C14.4911 3.89787 14.7555 3.24078 15.226 2.75632C15.6965 2.27185 16.3346 1.99967 17 1.99967"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                  <path
                    d="M19.5089 4.58301C19.5089 3.89787 19.2445 3.24078 18.774 2.75632C18.3035 2.27185 17.6654 1.99967 17 1.99967"
                    stroke="black"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
                <div
                  style={{
                    width: "15px",
                    height: "15px",
                    borderRadius: "50%",
                    backgroundColor: "#e55353",
                    color: "white",
                    fontSize: "0.7rem",
                    position: "absolute",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    top: 0,
                    left: "50%",
                    zIndex: 10,
                  }}
                >
                  <div>6</div>
                </div>
              </div>
            </div>
          </div>

          <div className="relative w-[40px] h-[40px] rounded-[50%]">
            <img
              src={imageLogo}
              alt="User Avatar"
              className="absolute left-0 right-0 object-cover object-center w-full h-full cursor-pointer rounded-[50%]"
              onClick={toggleDropdown}
            />

            {isDropdownAvatarOpen && (
              <div
                className="absolute right-0 mt-2 w-60 bg-white border border-gray-200 rounded-md shadow-lg pt-2"
                onMouseLeave={() => {
                  if (isDropdownAvatarOpen) {
                    toggleDropdown();
                  }
                }}
              >
                <div className="flex flex-col items-center justify-center py-2">
                  <div className="flex items-center justify-center h-12 w-12">
                    <img
                      src={imageLogo}
                      alt=""
                      className="border border-gray-300 h-full w-full rounded-full object-cover"
                    />
                  </div>
                  <div className="text-black text-lg font-medium">
                    Kevin Gilbert
                  </div>
                  <div className="text-black text-sm font-normal">
                    KevinGilbert@mgm-tp.com
                  </div>
                </div>
                <hr></hr>
                <div>
                  <a
                    className="flex items-center gap-4 px-4 py-2 text-gray-800 hover:bg-gray-100"
                    href="#"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="1.6rem"
                      height="1.6rem"
                      viewBox="0 0 22 22"
                      fill="none"
                    >
                      <path
                        d="M8.43189 0C7.31375 0 6.24141 0.515078 5.45077 1.43192C4.66012 2.34877 4.21595 3.59227 4.21595 4.88889C4.21595 6.1855 4.66012 7.42901 5.45077 8.34586C6.24141 9.2627 7.31375 9.77778 8.43189 9.77778C9.55003 9.77778 10.6224 9.2627 11.413 8.34586C12.2037 7.42901 12.6478 6.1855 12.6478 4.88889C12.6478 3.59227 12.2037 2.34877 11.413 1.43192C10.6224 0.515078 9.55003 0 8.43189 0ZM8.43189 2.44444C8.99096 2.44444 9.52713 2.70198 9.92245 3.16041C10.3178 3.61883 10.5399 4.24058 10.5399 4.88889C10.5399 5.5372 10.3178 6.15895 9.92245 6.61737C9.52713 7.07579 8.99096 7.33333 8.43189 7.33333C7.87282 7.33333 7.33665 7.07579 6.94133 6.61737C6.54601 6.15895 6.32392 5.5372 6.32392 4.88889C6.32392 4.24058 6.54601 3.61883 6.94133 3.16041C7.33665 2.70198 7.87282 2.44444 8.43189 2.44444ZM15.8098 9.77778C15.6412 9.77778 15.5568 9.87556 15.5568 10.0711L15.2828 11.6111C15.0509 11.8311 14.7136 12.0267 14.4607 12.2222L13.1116 11.6111C13.0273 11.6111 12.8586 11.6111 12.7743 11.7333L11.7625 13.8844C11.6782 13.9822 11.6782 14.1778 11.8468 14.2756L12.943 15.2778V16.5L11.8468 17.5022C11.7625 17.6 11.6782 17.7956 11.7625 17.8933L12.7743 20.0444C12.8586 20.1667 13.0273 20.1667 13.1116 20.1667L14.4607 19.5556C14.7136 19.7511 15.0509 19.9467 15.2828 20.1667L15.5568 21.7067C15.5568 21.9022 15.6412 22 15.8098 22H17.9178C18.0021 22 18.1707 21.9022 18.1707 21.7067L18.3394 20.1667C18.6766 19.9467 19.0139 19.7511 19.2669 19.5556L20.5527 20.1667C20.7003 20.1667 20.8689 20.1667 20.8689 20.0444L21.9651 17.8933C22.0494 17.7956 21.9651 17.6 21.8808 17.5022L20.7846 16.5V15.2778L21.8808 14.2756C21.9651 14.1778 22.0494 13.9822 21.9651 13.8844L20.8689 11.7333C20.8689 11.6111 20.7003 11.6111 20.5527 11.6111L19.2669 12.2222C19.0139 12.0267 18.6766 11.8311 18.3394 11.6111L18.1707 10.0711C18.1707 9.87556 18.0021 9.77778 17.9178 9.77778H15.8098ZM8.43189 11C5.61775 11 0 12.6256 0 15.8889V19.5556H10.192C9.89693 18.8344 9.68613 18.0522 9.58074 17.2333H2.00257V15.8889C2.00257 15.1067 5.30155 13.3222 8.43189 13.3222C8.8851 13.3222 9.34886 13.3711 9.80207 13.4444C10.0129 12.6622 10.2974 11.9289 10.6663 11.2567C9.84423 11.0978 9.06428 11 8.43189 11ZM16.9059 14.0556C17.7491 14.0556 18.4448 14.8622 18.4448 15.9378C18.4448 16.9156 17.7491 17.7222 16.9059 17.7222C15.9784 17.7222 15.2828 16.9156 15.2828 15.9378C15.2828 14.8622 15.9784 14.0556 16.9059 14.0556Z"
                        fill="#141718"
                      />
                    </svg>
                    <div className="font-medium w-full">My Profile</div>
                  </a>
                  <a
                    className="flex items-center gap-4 px-4 py-2 text-gray-800 hover:bg-gray-100"
                    href="/personal/registration"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="1.6rem"
                      height="1.6rem"
                      viewBox="0 0 22 22"
                      fill="none"
                    >
                      <path
                        d="M19.5556 2H17.1111V8.5L13.4444 6.25L9.77778 8.5V2H2.44444V20H19.5556V2ZM12.2222 2V4.5L13.4444 3.75L14.6667 4.5V2H12.2222ZM22 22H0V0H22V22Z"
                        fill="#141718"
                      />
                    </svg>
                    <div className="font-medium w-full">My Registration</div>
                  </a>
                  <a
                    className="flex items-center gap-4 px-4 py-2 text-gray-800 hover:bg-gray-100"
                    href=""
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="1.6rem"
                      height="1.6rem"
                      viewBox="0 0 22 22"
                      fill="none"
                    >
                      <path
                        d="M11.7859 0C11.5775 0 11.3776 0.082785 11.2303 0.230143C11.0829 0.377501 11.0002 0.577362 11.0002 0.785758V10.2149C11.0002 10.4233 11.0829 10.6231 11.2303 10.7705C11.3776 10.9178 11.5775 11.0006 11.7859 11.0006H21.2143C21.4227 11.0006 21.6225 10.9178 21.7699 10.7705C21.9172 10.6231 22 10.4233 22 10.2149C22 8.87342 21.7358 7.54512 21.2225 6.3058C20.7092 5.06648 19.9568 3.9404 19.0084 2.99186C18.0599 2.04333 16.9339 1.2909 15.6947 0.77756C14.4554 0.264215 13.1272 0 11.7859 0ZM12.5716 9.4291V1.60609C14.5843 1.78958 16.4692 2.67273 17.8983 4.10198C19.3275 5.53123 20.2105 7.41618 20.394 9.4291H12.5716ZM9.42877 3.96336C9.42877 3.85421 9.40604 3.74625 9.36201 3.64637C9.31799 3.5465 9.25364 3.45689 9.17307 3.38326C9.09249 3.30962 8.99746 3.25359 8.89404 3.21872C8.79061 3.18386 8.68105 3.17093 8.57235 3.18075C6.78794 3.34345 5.08682 4.0113 3.66828 5.10606C2.24973 6.20082 1.17248 7.67717 0.562725 9.36216C-0.0470333 11.0472 -0.164057 12.871 0.225362 14.6201C0.614781 16.3693 1.49452 17.9712 2.76152 19.2383C4.02853 20.5054 5.63034 21.3852 7.37933 21.7746C9.12832 22.1641 10.9521 22.047 12.6369 21.4372C14.3218 20.8274 15.798 19.7501 16.8927 18.3315C17.9874 16.9128 18.6552 15.2116 18.8179 13.427C18.8275 13.3186 18.8144 13.2093 18.7796 13.1062C18.7447 13.003 18.6888 12.9083 18.6153 12.8279C18.5419 12.7475 18.4526 12.6833 18.353 12.6392C18.2534 12.5952 18.1458 12.5724 18.0369 12.5721H9.42877V3.96336ZM1.57175 12.5721C1.57138 10.7607 2.19682 9.00481 3.34224 7.60158C4.48765 6.19835 6.08267 5.23398 7.85736 4.8717V13.3579C7.85736 13.5663 7.94014 13.7661 8.08749 13.9135C8.23484 14.0609 8.43468 14.1436 8.64306 14.1436H17.1286C16.7343 16.0473 15.6487 17.7373 14.0813 18.8873C12.5139 20.0372 10.576 20.5655 8.64194 20.3701C6.70785 20.1748 4.91476 19.2696 3.60899 17.8294C2.30323 16.3892 1.57742 14.5162 1.57175 12.5721Z"
                        fill="#141718"
                      />
                    </svg>
                    <div className="font-medium w-full">Leader Board</div>
                  </a>
                </div>
                <hr></hr>
                <div>
                  <a
                    className="flex items-center gap-4 px-4 py-2 text-gray-800 hover:bg-gray-100"
                    href="#"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="1.6rem"
                      height="1.6rem"
                      viewBox="0 0 24 24"
                      fill="none"
                    >
                      <path
                        d="M23 12H5.4M23 12L15.96 4.66667M23 12L15.96 19.3333M8.04 23H1V1H8.04"
                        stroke="#141718"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                      />
                    </svg>
                    <div className="font-medium w-full">Sign out</div>
                  </a>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};
export default HeaderHomepage;
