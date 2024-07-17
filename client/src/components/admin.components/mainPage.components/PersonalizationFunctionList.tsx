import React from "react";

import functionList from "../../../utils/functionList";
import {useDispatch} from "react-redux";
import {AppDispatch} from "../../../redux/store/store.ts";
import {useNavigate} from "react-router-dom";

import functionMapping from "../../../utils/functionMapping.ts";

const PersonalizationFunctionList = () => {
    const dispatch = useDispatch<AppDispatch>();
    const navigate = useNavigate();

    const handleFunctionMapping = (e: React.MouseEvent, itemAlt: string) => {
        functionMapping(e, navigate, dispatch, itemAlt);
    }

    const handleGap = (alt: string) => {
        return alt == 'sign_out' ? '1.5' : '2.5'
    }

    return (
        <div
            className="function_list flex-col pl-3 text-lg"
            style={{justifyItems: "space-between", gap: "100px"}}
        >
            {functionList.map((item) => (
                <div
                    className={`
                        function flex align-center justify-start pb-2 cursor-pointer
                        gap-${handleGap(item.alt)}
                    `}
                    key={item.key}
                    onClick={(e) => {
                        handleFunctionMapping(e, item.alt)
                    }}
                >
                    <div className="icon flex items-center">
                        <img className="h-[70%]" src={item.imgSrc} alt={item.alt}/>
                    </div>
                    <span>{item.message}</span>
                </div>
            ))}
        </div>
    )
}

export default PersonalizationFunctionList;