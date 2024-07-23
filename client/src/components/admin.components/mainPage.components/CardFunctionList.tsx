import RegistrationList from "../../../assets/images/admin.images/RegistrationList.svg";
import functionList from "../../../utils/functionList";
import functionMapping from "../../../utils/functionMapping.ts";
import { AppDispatch } from "../../../redux/store/store.ts";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";

function CardFunctionList() {
    const convertedFunctionList = functionList.map((item) => {
        return item.alt == "registration_list"
            ? { ...item, imgSrc: RegistrationList }
            : item
    })

    const setItemGap = (alt: string) => {
        return alt == 'sign_out' ? '1' : '2.5'
    }

    const setItemColor = (alt: string) => {
        return alt == 'registration_list' ? 'text-[#861FA2]' : ''
    }

    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();

    const handleFunctionMapping = (e: React.MouseEvent, itemAlt: string) => {
        functionMapping(e, navigate, dispatch, itemAlt);
    }

    return (
        <div
            className="flex-col function_list"
            style={{ justifyItems: "space-between", gap: "100px" }}
        >
            {convertedFunctionList.map((item) => (
                <div
                    className={`function flex align-center justify-start pb-2 cursor-pointer font-semibold
                        gap-${setItemGap(item.alt)}
                        ${setItemColor(item.alt)}`
                    }
                    onClick={(e) => {
                        handleFunctionMapping(e, item.alt)
                    }}
                    key={item.key}

                >
                    <div className="flex items-center icon">
                        <img className="h-[70%]" src={item.imgSrc} alt={item.alt} />
                    </div>
                    <span>{item.message}</span>
                </div>
            ))}
        </div>
    );
}

export default CardFunctionList;
