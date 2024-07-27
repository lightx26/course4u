type DocumentType = {
  id: number;
  registrationId: number;
  url: string;
  status: string;
  type: string;
};
interface IProps {
  documentRegistration: DocumentType[];
  setDocumentRegistration: (value: DocumentType[]) => void;
  status?: string;
}
import type { CollapseProps } from "antd";
import { Collapse, Tag } from "antd";

const VerifyDocumentForAccountant = (props: IProps) => {
  const { documentRegistration, setDocumentRegistration, status } = props;
  const handlePreview = (url: string) => {
    const backendUrl = import.meta.env.VITE_BACKEND_URL;
    const sanitizedUrl = url.startsWith("/") ? url.slice(1) : url;
    const urlPreview = `${backendUrl}/${sanitizedUrl.slice(4)}`;
    window.open(urlPreview, "_blank");
  };

  const capitalizeFirstLetter = (text: string) => {
    if (!text) return "";
    return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
  };
  const changeStatusDocumentToApprove = (documentId: number) => {
    const updatedDocument = documentRegistration?.map((item) => {
      if (item.id === documentId) {
        if (item.status === "APPROVED") return { ...item, status: "PENDING" };
        return { ...item, status: "APPROVED" };
      }
      return item;
    });
    setDocumentRegistration(updatedDocument);
  };
  const changeStatusDocumentToDecline = (documentId: number) => {
    const updatedDocument = documentRegistration?.map((item) => {
      if (item.id === documentId) {
        if (item.status === "REFUSED") return { ...item, status: "PENDING" };
        return { ...item, status: "REFUSED" };
      }
      return item;
    });
    setDocumentRegistration(updatedDocument);
  };
  const items: CollapseProps["items"] = [
    {
      key: "1",
      label: (
        <span style={{ fontSize: "1.1rem", fontWeight: "500" }}>Document</span>
      ),
      showArrow: false,
      children: (
        <>
          <div>
            <Tag color="magenta">PDF</Tag>
            <Tag color="magenta">DOCX</Tag>
            <Tag color="magenta">JPEG</Tag>
            <Tag color="magenta">JPG</Tag>
            <Tag color="magenta">PNG</Tag>
          </div>
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              width: "100%",
              gap: "10px",
              marginTop: "20px",
            }}
          >
            {documentRegistration?.map((item) => (
              <div
                key={item.id}
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  border: `1px solid ${
                    item.status === "PENDING"
                      ? "#ccc"
                      : item.status === "APPROVED"
                      ? "#34A853"
                      : "#f71414"
                  }`,
                  padding: "10px 20px",
                  height: "60px",
                  alignItems: "center",
                  borderRadius: "10px",
                }}
              >
                <div style={{ flex: "3" }}>
                  <div
                    style={{
                      fontWeight: 500,
                      fontSize: "0.9rem",
                      cursor: "pointer",
                    }}
                  >
                    {item?.url?.split("/").pop()}
                  </div>
                  <div
                    style={{
                      fontWeight: 500,
                      fontSize: "0.8rem",
                      color: "#3276e8",
                      cursor: "pointer",
                    }}
                    onClick={() => handlePreview(item.url)}
                  >
                    Preview
                  </div>
                </div>
                <div style={{ flex: "3", fontWeight: 500, fontSize: "1rem" }}>
                  {capitalizeFirstLetter(item.type)}
                </div>
                {status === "VERIFYING" && (
                  <div
                    style={{
                      flex: "1",
                      display: "flex",
                      gap: "30px",
                      alignItems: "center",
                    }}
                  >
                    <div
                      style={{
                        cursor: "pointer",
                        width: "40px",
                        height: "40px",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        backgroundColor: `${
                          item.status === "APPROVED" ? "#c2e5c5" : "white"
                        }`,
                        borderRadius: "8px",
                      }}
                      className="hover:bg-slate-100"
                      onClick={() => changeStatusDocumentToApprove(item.id)}
                    >
                      <svg
                        width="28"
                        height="20"
                        viewBox="0 0 35 27"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                      >
                        <path
                          d="M1.5 14.2507L12.1667 25.4507L33.5 1.45068"
                          stroke="#34A853"
                          stroke-width="2"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                        />
                      </svg>
                    </div>
                    <div
                      style={{
                        cursor: "pointer",
                        width: "40px",
                        height: "40px",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        backgroundColor: `${
                          item.status === "REFUSED" ? "#ffbaba" : "white"
                        }`,
                        borderRadius: "8px",
                      }}
                      className="hover:bg-slate-100"
                      onClick={() => changeStatusDocumentToDecline(item.id)}
                    >
                      <svg
                        width="20"
                        height="23"
                        viewBox="0 0 23 26"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                      >
                        <path
                          d="M3.64813 10.3257H16.2406V11.8882H3.64813V10.3257ZM16.2406 7.20068H3.64813V5.63818H16.2406V7.20068ZM11.5184 24.3882L13.0802 25.9507H0.5V0.950684H19.3888V17.4058L18.6017 18.1748L17.8147 17.3936V2.51318H2.07406V24.3882H11.5184ZM18.6017 25.9141L18.6386 25.9507H18.5648L18.6017 25.9141ZM16.2406 15.0132L14.6543 16.5757H3.64813V15.0132H16.2406ZM12.3424 19.7007L13.9164 21.2632H3.64813V19.7007H12.3424ZM19.7208 22.0444L22.5 24.8154L21.3932 25.9141L18.6017 23.1553L15.8102 25.9263L14.7035 24.8154L17.495 22.0444L14.7035 19.2734L15.8102 18.1748L18.6017 20.9458L21.3932 18.1748L22.5 19.2856L19.7208 22.0444Z"
                          fill="#F71414"
                        />
                      </svg>
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        </>
      ),
    },
  ];

  return (
    <>
      {documentRegistration && documentRegistration.length > 0 && (
        <div>
          <Collapse items={items} defaultActiveKey={["1"]} />
        </div>
      )}
    </>
  );
};
export default VerifyDocumentForAccountant;
