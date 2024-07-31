import RegistrationList from "../../../assets/images/admin.images/RegistrationList.svg";
import functionList from "../../../utils/functionList";
import functionMapping from "../../../utils/functionMapping.ts";
import {RootState} from "../../../redux/store/store.ts";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import CourseList from "../../../assets/images/admin.images/CourseList.svg";

import {refreshAdmin} from "../../../redux/slice/adminRegistration.slice.ts";
import {refreshAccountant} from "../../../redux/slice/accountantRegistration.slice.ts";

function CardFunctionList() {
    const convertedFunctionList = functionList.map((item) => {
        if (
            location.pathname.includes("/admin/courses/create") &&
            item.alt === "registration_list"
        ) {
            return {...item, imgSrc: CourseList};
        }
        return item.alt == "registration_list"
            ? {...item, imgSrc: RegistrationList}
            : item;
    });

    const setItemGap = (alt: string) => {
        return alt == "sign_out" ? "1" : "2.5";
    };

    const setItemColor = (alt: string) => {
        if (location.pathname.includes("/admin/courses/create")) {
            return;
        }
        return alt == "registration_list" ? "text-[#861FA2]" : "";
    };

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const userRole = useSelector((state: RootState) => state.user.user.role);

    const handleFunctionMapping = (e: React.MouseEvent, itemAlt: string) => {
        functionMapping(e, navigate, dispatch, itemAlt, userRole);
    };

    const handleRefresh = (userRole: string) => {
        if (userRole === 'ADMIN'){
            dispatch(refreshAdmin());
        }
        else{
            dispatch(refreshAccountant());
        }
    }

    return (
        <div
            className="flex-col function_list"
            style={{justifyItems: "space-between", gap: "100px"}}
        >
            {convertedFunctionList.map((item, index) =>
                index === 1 && userRole === "ACCOUNTANT" ? null : (
                    <div
                        className={`function flex align-center justify-start pb-2 cursor-pointer font-semibold
                        gap-${setItemGap(item.alt)}
                        ${setItemColor(item.alt)}`}
                        onClick={(e) => {
                            handleFunctionMapping(e, item.alt);
                            handleRefresh(userRole);
                        }}
                        key={item.key}
                    >
                        <div className="flex items-center icon">
                            <img className="h-[70%]" src={item.imgSrc} alt={item.alt}/>
                        </div>
                        <span>{item.message}</span>
                    </div>
                )
            )}
        </div>
    );
}

export default CardFunctionList;
