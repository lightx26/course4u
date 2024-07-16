import React from "react";
import SignOut from "../../../assets/images/admin.images/SignOut.svg";

import {AppDispatch} from "../../../redux/store/store.ts";
import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import {handleLogout} from "../../../redux/slice/user.slice.ts";

import functionList from "../../../utils/functionList.ts";

const PersonalizationFunctionList = () => {
    const dispatch = useDispatch<AppDispatch>();
    const navigate = useNavigate();

    const handleSignoutClick = (e: React.MouseEvent) => {
        e.preventDefault();
        navigate("/login");
        dispatch(handleLogout());
    }

    return (
        <div
            className="function_list flex-col pl-3 text-lg"
            style={{justifyItems: "space-between", gap: "100px"}}
        >
            {functionList.map((item) => (
                <div
                    className="function flex align-center justify-start pb-2 gap-1 cursor-pointer"
                >
                    <div className="icon flex items-center">
                        <img className="h-[70%]" src={item.imgSrc} alt={item.alt}/>
                    </div>
                    <span>{item.message}</span>
                </div>
            ))}
            <div className="function flex align-center justify-start gap-1 cursor-pointer"
                 onClick={(e) => handleSignoutClick(e)}>
                <div className="icon flex items-center">
                    <img className="h-[70%]" src={SignOut} alt="registration_list_icon"/>
                </div>
                <span>Sign out</span>
            </div>
        </div>
    )
}

export default PersonalizationFunctionList;