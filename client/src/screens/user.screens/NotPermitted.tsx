import { Button, Result } from "antd";
import "../../assets/css/NotPermitted.css";
import { useNavigate } from "react-router-dom";
import { RootState } from "../../redux/store/store";
import { useSelector } from "react-redux";
const NotPermitted = () => {
  const navigate = useNavigate();
  const userRole = useSelector((state: RootState) => state.user.user.role);
  return (
    <>
      <Result
        status="403"
        title="403"
        subTitle="Sorry, you are not authorized to access this page."
        extra={
          <Button
            type="primary"
            onClick={() => {
              if (userRole === "USER") {
                navigate(`${import.meta.env.VITE_BASE_URL}/`);
              } else if (userRole === "ADMIN") {
                navigate(`${import.meta.env.VITE_BASE_URL}/admin`);
              } else if (userRole === "ACCOUNTANT") {
                navigate(`${import.meta.env.VITE_BASE_URL}/accountant`);
              }
            }}
            style={{ backgroundColor: "#861fa2" }}
          >
            Back Home
          </Button>
        }
        className="custom-result"
      />
    </>
  );
};
export default NotPermitted;
