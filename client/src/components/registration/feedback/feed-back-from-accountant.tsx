import { Input } from "antd";
import { RootState } from "../../../redux/store/store";
import { useSelector } from "react-redux";
import { handleAvatarUrl } from "../../../utils/handleAvatarUrl";
type IProps = {
    setFeedBackFromAccountant: (value: string) => void;
};
const FeedBackFromAccountant = (props: IProps) => {
    const { TextArea } = Input;
    const { setFeedBackFromAccountant } = props;
    const onChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => {
        setFeedBackFromAccountant(e.target.value);
    };

    const userData = useSelector((state: RootState) => state.user.user);

    return (
        <div className='flex justify-start gap-5 align-top'>
            <div
                style={{
                    width: "50px",
                    height: "50px",
                    position: "relative",
                    borderRadius: "50%",
                }}
            >
                <img
                    src={handleAvatarUrl(userData.avatarUrl)}
                    alt=''
                    style={{
                        width: "100%",
                        height: "100%",
                        position: "absolute",
                        left: 0,
                        right: 0,
                        objectFit: "cover",
                        objectPosition: "center",
                        borderRadius: "50%",
                    }}
                />
            </div>
            <div style={{ width: "100%" }}>
                <div style={{ marginBottom: "10px", fontWeight: 500 }}>
                    {userData.fullName || "Anonymous"}
                </div>
                <TextArea
                    onChange={onChange}
                    placeholder='Write your feedback here!!!'
                    style={{ height: 120 }}
                    allowClear
                />
            </div>
        </div>
    );
};
export default FeedBackFromAccountant;
