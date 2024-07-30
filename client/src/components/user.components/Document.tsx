import { Tag, message, Divider, Upload } from "antd";
import type { UploadProps, UploadFile } from "antd";
import React from "react";
import "../../assets/css/Document.css";
import ReactDOM from "react-dom";
import { Collapse } from "antd";
import type { CollapseProps } from "antd";
type DocumentType = {
  id: number;
  registrationId: number;
  url: string;
  status: string;
  type: string;
};
interface IProps {
  listFileCertificate: UploadFile[];
  setListFileCertificate: React.Dispatch<React.SetStateAction<UploadFile[]>>;
  listFilePayment: UploadFile[];
  setListFilePayment: React.Dispatch<React.SetStateAction<UploadFile[]>>;
  documentRegistrationResubmit?: DocumentType[];
  setDocumentRegistrationResubmit?: React.Dispatch<
    React.SetStateAction<DocumentType[]>
  >;
  setListIdDocumentRemove?: React.Dispatch<React.SetStateAction<number[]>>;
}
const Document: React.FC<IProps> = (props) => {
  const {
    listFileCertificate,
    setListFileCertificate,
    listFilePayment,
    setListFilePayment,
    documentRegistrationResubmit,
    setDocumentRegistrationResubmit,
    setListIdDocumentRemove,
  } = props;
  const { Dragger } = Upload;

  const handleChange1: UploadProps["onChange"] = (info) => {
    const { status } = info.file;
    if (status === "done") {
      message.success(`${info.file.name} file uploaded successfully.`);
      setListFileCertificate([...info.fileList]);
    } else if (status === "error") {
      message.error(`${info.file.name} file upload failed.`);
    }
  };

  const handleChange2: UploadProps["onChange"] = (info) => {
    const { status } = info.file;
    if (status === "done") {
      message.success(`${info.file.name} file uploaded successfully.`);
      setListFilePayment([...info.fileList]);
    } else if (status === "error") {
      message.error(`${info.file.name} file upload failed.`);
    }
  };

  const customRequest: UploadProps["customRequest"] = (options) => {
    const { onSuccess } = options;

    if (onSuccess) {
      onSuccess("ok");
    }
  };

  type FileType = Parameters<NonNullable<UploadProps["beforeUpload"]>>[0];

  const beforeUpload = (file: FileType) => {
    const allowedTypes = [
      "application/pdf",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      "image/jpeg",
      "image/png",
      "image/jpg",
    ];
    const isAllowedType = allowedTypes.includes(file.type);
    if (!isAllowedType) {
      message.error("You can only upload PDF, DOCX, JPG, JPEG, PNG files!");
      return Upload.LIST_IGNORE;
    }
    const isLt10M = file.size / 1024 / 1024 < 10;
    if (!isLt10M) {
      message.error("File must be smaller than 10MB!");
      return Upload.LIST_IGNORE;
    }
    return true;
  };

  const UploadButtonCertificate = (
    <button
      style={{ border: 0, background: "none", width: "100%", height: "100%" }}
      type="button"
    >
      <div
        style={{
          width: "100%",
          height: "100%",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          fontWeight: "500",
          fontSize: "1rem",
          color: "#909090",
          flexDirection: "column",
        }}
      >
        <svg
          width="39"
          height="44"
          viewBox="0 0 39 44"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M21.542 1.53394H22.0988C28.7571 1.53394 32.0863 1.53394 34.3982 3.16285C35.0606 3.62956 35.6487 4.18305 36.1446 4.8065C37.8753 6.98247 37.8753 10.1158 37.8753 16.3824V21.5794C37.8753 27.6292 37.8753 30.6541 36.9179 33.07C35.3787 36.9539 32.1237 40.0175 27.997 41.4662C25.4301 42.3673 22.2161 42.3673 15.7882 42.3673C12.1151 42.3673 10.2786 42.3673 8.81174 41.8524C6.45365 41.0246 4.59361 39.2739 3.71408 37.0546C3.16699 35.674 3.16699 33.9455 3.16699 30.4885V21.9506"
            stroke="#3276E8"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M37.8756 21.9507C37.8756 25.7093 34.8287 28.7562 31.0701 28.7562C29.7108 28.7562 28.1082 28.5181 26.7866 28.8722C25.6123 29.1868 24.6951 30.104 24.3805 31.2783C24.0264 32.5999 24.2645 34.2025 24.2645 35.5618C24.2645 39.3204 21.2176 42.3673 17.459 42.3673"
            stroke="#3276E8"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M17.4583 9.7006L1.125 9.7006M9.29167 1.53394V17.8673"
            stroke="#3276E8"
            stroke-width="2"
            stroke-linecap="round"
          />
        </svg>
        Click to upload certificate
      </div>
    </button>
  );

  const UploadButtonPayment = (
    <button
      style={{ border: 0, background: "none", width: "100%", height: "100%" }}
      type="button"
    >
      <div
        style={{
          width: "100%",
          height: "100%",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          fontWeight: "500",
          fontSize: "1rem",
          color: "#909090",
        }}
      >
        <svg
          width="39"
          height="44"
          viewBox="0 0 39 44"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M21.542 1.53394H22.0988C28.7571 1.53394 32.0863 1.53394 34.3982 3.16285C35.0606 3.62956 35.6487 4.18305 36.1446 4.8065C37.8753 6.98247 37.8753 10.1158 37.8753 16.3824V21.5794C37.8753 27.6292 37.8753 30.6541 36.9179 33.07C35.3787 36.9539 32.1237 40.0175 27.997 41.4662C25.4301 42.3673 22.2161 42.3673 15.7882 42.3673C12.1151 42.3673 10.2786 42.3673 8.81174 41.8524C6.45365 41.0246 4.59361 39.2739 3.71408 37.0546C3.16699 35.674 3.16699 33.9455 3.16699 30.4885V21.9506"
            stroke="#3276E8"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M37.8756 21.9507C37.8756 25.7093 34.8287 28.7562 31.0701 28.7562C29.7108 28.7562 28.1082 28.5181 26.7866 28.8722C25.6123 29.1868 24.6951 30.104 24.3805 31.2783C24.0264 32.5999 24.2645 34.2025 24.2645 35.5618C24.2645 39.3204 21.2176 42.3673 17.459 42.3673"
            stroke="#3276E8"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path
            d="M17.4583 9.7006L1.125 9.7006M9.29167 1.53394V17.8673"
            stroke="#3276E8"
            stroke-width="2"
            stroke-linecap="round"
          />
        </svg>
        Click to upload payment
      </div>
    </button>
  );

  const props1: UploadProps = {
    name: "file",
    onChange: handleChange1,
    customRequest: customRequest,
    beforeUpload,
    defaultFileList: [...listFileCertificate],
    itemRender(__, file, _, { remove }) {
      return ReactDOM.createPortal(
        <div className="file-list-item" key={file.uid}>
          <div className="file-list-item-info" style={{ width: "50%" }}>
            <div
              className="file-list-item-info-name"
              style={{
                fontWeight: "500",
                display: "-webkit-box",
                WebkitLineClamp: 1,
                WebkitBoxOrient: "vertical",
                textOverflow: "ellipsis",
                whiteSpace: "normal",
                wordWrap: "break-word",
                overflow: "hidden",
              }}
            >
              {file.name}
            </div>
            <div
              className="file-list-item-info-name"
              style={{ fontSize: "0.8rem", color: "#3276e8" }}
            >
              Upload complete
            </div>
          </div>
          <div style={{ fontWeight: "500", width: "25%" }}>Certificate</div>
          <div className="file-list-item-action">
            <svg
              width="17"
              height="19"
              viewBox="0 0 17 19"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
              style={{ color: "red", cursor: "pointer" }}
              onClick={() => {
                remove();
                setListFileCertificate(
                  listFileCertificate.filter((item) => item.uid !== file.uid)
                );
              }}
            >
              <path
                d="M3.5 18.4508C2.95 18.4508 2.47933 18.2551 2.088 17.8638C1.69667 17.4725 1.50067 17.0015 1.5 16.4508V3.45081H0.5V1.45081H5.5V0.450806H11.5V1.45081H16.5V3.45081H15.5V16.4508C15.5 17.0008 15.3043 17.4718 14.913 17.8638C14.5217 18.2558 14.0507 18.4515 13.5 18.4508H3.5ZM13.5 3.45081H3.5V16.4508H13.5V3.45081ZM5.5 14.4508H7.5V5.45081H5.5V14.4508ZM9.5 14.4508H11.5V5.45081H9.5V14.4508Z"
                fill="#EC5454"
              />
            </svg>
          </div>
        </div>,
        document.querySelector(".div_itemRender") as Element
      );
    },
    progress: {
      strokeColor: {
        "0%": "#108ee9",
        "100%": "#87d068",
      },
      strokeWidth: 3,
      format: (percent) => percent && `${parseFloat(percent.toFixed(2))}%`,
    },
  };

  const props2: UploadProps = {
    name: "file",
    onChange: handleChange2,
    customRequest: customRequest,
    beforeUpload,
    defaultFileList: [...listFilePayment],
    itemRender(__, file, _, { remove }) {
      return ReactDOM.createPortal(
        <div
          className="file-list-item"
          key={file.uid}
          style={{
            border: "1px solid #ccc",
            margin: "10px 0px 5px 0px",
            borderRadius: "5px",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            height: "50px",
            padding: "0px 20px",
          }}
        >
          <div className="file-list-item-info" style={{ width: "50%" }}>
            <div
              className="file-list-item-info-name"
              style={{
                fontWeight: "500",
                display: "-webkit-box",
                WebkitLineClamp: 1,
                WebkitBoxOrient: "vertical",
                textOverflow: "ellipsis",
                whiteSpace: "normal",
                wordWrap: "break-word",
                overflow: "hidden",
              }}
            >
              {file.name}
            </div>
            <div
              className="file-list-item-info-name"
              style={{ fontSize: "0.8rem", color: "#3276e8" }}
            >
              Upload complete
            </div>
          </div>
          <div style={{ fontWeight: "500", width: "25%" }}>Payment</div>
          <div className="file-list-item-action">
            <svg
              width="17"
              height="19"
              viewBox="0 0 17 19"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
              style={{ color: "red", cursor: "pointer" }}
              onClick={() => {
                remove();
                setListFilePayment(
                  listFilePayment.filter((item) => item.uid !== file.uid)
                );
              }}
            >
              <path
                d="M3.5 18.4508C2.95 18.4508 2.47933 18.2551 2.088 17.8638C1.69667 17.4725 1.50067 17.0015 1.5 16.4508V3.45081H0.5V1.45081H5.5V0.450806H11.5V1.45081H16.5V3.45081H15.5V16.4508C15.5 17.0008 15.3043 17.4718 14.913 17.8638C14.5217 18.2558 14.0507 18.4515 13.5 18.4508H3.5ZM13.5 3.45081H3.5V16.4508H13.5V3.45081ZM5.5 14.4508H7.5V5.45081H5.5V14.4508ZM9.5 14.4508H11.5V5.45081H9.5V14.4508Z"
                fill="#EC5454"
              />
            </svg>
          </div>
        </div>,
        document.querySelector(".div_itemRender") as Element
      );
    },
    progress: {
      strokeColor: {
        "0%": "#108ee9",
        "100%": "#87d068",
      },
      strokeWidth: 3,
      format: (percent) => percent && `${parseFloat(percent.toFixed(2))}%`,
    },
  };

  const capitalizeFirstLetter = (text: string) => {
    if (!text) return "";
    return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
  };

  const handlePreview = (url: string) => {
    const backendUrl = import.meta.env.VITE_BACKEND_URL;
    const sanitizedUrl = url.startsWith("/") ? url.slice(1) : url;
    const urlPreview = `${backendUrl}/${sanitizedUrl.slice(4)}`;
    window.open(urlPreview, "_blank");
  };

  return (
    <>
      <div className="div_document">
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
            justifyContent: "center",
            alignItems: "center",
            gap: "50px",
            marginTop: "20px",
            height: "250px",
            padding: "0px 50px 20px 50px",
          }}
        >
          <div
            style={{
              height: "200px",
              width: "40%",
              border: "1px solid #ccc",
              borderRadius: "10px",
            }}
          >
            <Dragger
              {...props1}
              style={{ backgroundColor: "white", border: "1px solid #ccc" }}
            >
              <>{UploadButtonCertificate}</>
            </Dragger>
          </div>
          <Divider
            type="vertical"
            style={{
              height: "100%",
              color: "#cccccc",
            }}
          />
          <div
            style={{
              height: "200px",
              width: "40%",
              border: "1px solid #ccc",
              borderRadius: "10px",
            }}
          >
            <Dragger
              {...props2}
              style={{ backgroundColor: "white", border: "1px solid #ccc" }}
            >
              <>{UploadButtonPayment}</>
            </Dragger>
          </div>
        </div>
        <div className="div_itemRender" style={{ padding: "0px 100px" }}>
          {documentRegistrationResubmit &&
            documentRegistrationResubmit.length > 0 &&
            documentRegistrationResubmit.map((item) => (
              <div
                className="file-list-item"
                key={item.id}
                style={{
                  borderColor: `${
                    item.status === "PENDING"
                      ? "#ccc"
                      : item.status === "APPROVED"
                      ? "#34A853"
                      : "#f71414"
                  }`,
                }}
              >
                <div
                  className="file-list-item-info"
                  style={{
                    width: "50%",
                  }}
                >
                  <div
                    className="file-list-item-info-name"
                    style={{
                      fontWeight: "500",
                      display: "-webkit-box",
                      WebkitLineClamp: 1,
                      WebkitBoxOrient: "vertical",
                      textOverflow: "ellipsis",
                      whiteSpace: "normal",
                      wordWrap: "break-word",
                      overflow: "hidden",
                    }}
                  >
                    {item?.url?.split("/").pop()}
                  </div>
                  <div
                    className="file-list-item-info-name"
                    style={{
                      fontSize: "0.8rem",
                      color: "#3276e8",
                      cursor: "pointer",
                    }}
                    onClick={() => handlePreview(item.url)}
                  >
                    Preview
                  </div>
                </div>
                <div style={{ fontWeight: "500", width: "25%" }}>
                  {capitalizeFirstLetter(item.type)}
                </div>
                <div className="file-list-item-action">
                  <svg
                    width="17"
                    height="19"
                    viewBox="0 0 17 19"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                    style={{ color: "red", cursor: "pointer" }}
                    onClick={() => {
                      if (setDocumentRegistrationResubmit) {
                        setDocumentRegistrationResubmit((prev) => {
                          return prev.filter((i) => i.id !== item.id);
                        });
                      }

                      if (setListIdDocumentRemove) {
                        setListIdDocumentRemove((prev) => [...prev, item.id]);
                      }
                    }}
                  >
                    <path
                      d="M3.5 18.4508C2.95 18.4508 2.47933 18.2551 2.088 17.8638C1.69667 17.4725 1.50067 17.0015 1.5 16.4508V3.45081H0.5V1.45081H5.5V0.450806H11.5V1.45081H16.5V3.45081H15.5V16.4508C15.5 17.0008 15.3043 17.4718 14.913 17.8638C14.5217 18.2558 14.0507 18.4515 13.5 18.4508H3.5ZM13.5 3.45081H3.5V16.4508H13.5V3.45081ZM5.5 14.4508H7.5V5.45081H5.5V14.4508ZM9.5 14.4508H11.5V5.45081H9.5V14.4508Z"
                      fill="#EC5454"
                    />
                  </svg>
                </div>
              </div>
            ))}
        </div>
      </div>
    </>
  );
};

const FormDocument: React.FC<IProps> = (props) => {
  const {
    listFileCertificate,
    setListFileCertificate,
    listFilePayment,
    setListFilePayment,
    documentRegistrationResubmit,
    setDocumentRegistrationResubmit,
    setListIdDocumentRemove,
  } = props;
  const items: CollapseProps["items"] = [
    {
      key: "1",
      label: (
        <h1 style={{ fontWeight: 500, fontSize: "1.1rem" }}>
          Document <span style={{ color: "red" }}>*</span>
        </h1>
      ),
      children: (
        <Document
          listFileCertificate={listFileCertificate}
          setListFileCertificate={setListFileCertificate}
          listFilePayment={listFilePayment}
          setListFilePayment={setListFilePayment}
          documentRegistrationResubmit={documentRegistrationResubmit}
          setDocumentRegistrationResubmit={setDocumentRegistrationResubmit}
          setListIdDocumentRemove={setListIdDocumentRemove}
        />
      ),
      showArrow: false,
    },
  ];
  return (
    <>
      <Collapse defaultActiveKey={["1"]} items={items} />
    </>
  );
};

export default FormDocument;
