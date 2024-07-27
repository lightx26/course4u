import { Input } from "antd";
import { RootState } from "../redux/store/store";
import { useSelector } from "react-redux";
import { handleAvatarUrl } from "../utils/handleAvatarUrl";
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
    <div className="flex justify-start align-top gap-5">
      <div
        style={{
          width: "50px",
          height: "50px",
          position: "relative",
          borderRadius: "50%",
        }}
      >
        <img
          src={
            userData.avatarUrl !== "" && userData.avatarUrl !== null
              ? userData.avatarUrl
              : handleAvatarUrl(userData.avatarUrl)
          }
          alt=""
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
          {userData.fullName}
        </div>
        <TextArea
          onChange={onChange}
          placeholder="Write your feedback here!!!"
          style={{ height: 120, resize: "none" }}
          allowClear
        />
      </div>
    </div>
  );
};
export default FeedBackFromAccountant;
