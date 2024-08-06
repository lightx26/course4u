import { useRegistrationModal } from "../../../hooks/use-registration-modal";
import { Status } from "../../../constant/index";
import { Button } from "../../ui/button";
import { toast } from "sonner";
import { useRefreshState } from "../../../hooks/use-refresh-state";
import ModalConfirm from "../../modal/modal-confirm";
import { approve_Decline_Document_Registration } from "../../../service/registration";
type DocumentType = {
    id: number;
    registrationId: number;
    url: string;
    status: string;
    type: string;
};

type IProps = {
    status?: Status;
    id: number | undefined;
    feedBackFromAccountant: string;
    document: DocumentType[];
};
export const RegistrationButtonForAccountant = (props: IProps) => {
    const { status, id, feedBackFromAccountant, document } = props;
    const { close } = useRegistrationModal((store) => store);
    const { setRegistrationFlagAccountant } = useRefreshState((state) => state);

    const handleApproveDocument = async () => {
        if (document && document.length > 0) {
            for (let i = 0; i < document.length; i++) {
                if (
                    document[i].status === "PENDING" ||
                    document[i].status === "REFUSED"
                ) {
                    return toast.error("Failed to approve registration", {
                        style: { color: "red" },
                        description: `Please approve all document before approve registration`,
                    });
                }
            }
        }
        const trimmedFeedback = feedBackFromAccountant.trim();
        const response = await approve_Decline_Document_Registration(
            id,
            trimmedFeedback,
            document,
            "VERIFIED"
        );
        if (response && response.status === 200) {
            toast.success("Registration approved", {
                style: { color: "green" },
                description: "Registration approved successfully",
            });

            //dispatch action fetch list registration
            setRegistrationFlagAccountant();

            close();
        } else if (response && response.status !== 200 && response.data) {
            toast.error("Failed to approve registration", {
                style: { color: "red" },
                description: `${response.data.message}`,
            });
        }
    };
    const handleDeclineDocument = async () => {
        const trimmedFeedback = feedBackFromAccountant.trim();
        if (trimmedFeedback === "" || trimmedFeedback === undefined) {
            return toast.error("Failed to declined registration", {
                style: { color: "red" },
                description: `Please provide feedback`,
            });
        }
        const response = await approve_Decline_Document_Registration(
            id,
            trimmedFeedback,
            document,
            "DOCUMENT_DECLINED"
        );
        if (response && response.status === 200) {
            toast.success("Registration declined", {
                style: { color: "green" },
                description: "Registration declined successfully",
            });

            //dispatch action fetch list registration
            setRegistrationFlagAccountant();

            close();
        } else if (response && response.status !== 200 && response.data) {
            toast.error("Failed to declined registration", {
                style: { color: "red" },
                description: `${response.data.message}`,
            });
        }
    };
    return (
        <div className='flex justify-end gap-4'>
            {status === Status.VERIFYING && (
                <>
                    <ModalConfirm
                        title='Decline this registration'
                        description='Are you sure you want to decline this registration?'
                        handleConfirm={handleDeclineDocument}
                    >
                        <Button
                            size='default'
                            variant='danger'
                            type='button'
                            disabled={
                                feedBackFromAccountant.trim() === ""
                                    ? true
                                    : false
                            }
                        >
                            Decline and send feedback
                        </Button>
                    </ModalConfirm>

                    {/* <Modal
            title={
              <p style={{ fontSize: "1.2rem" }}>Approve this registration</p>
            }
            open={openModalConfirmApprove}
            onOk={handleApproveDocument}
            onCancel={() => setOpenModalConfirmApprove(false)}
            okText="Yes"
            cancelText="No"
            centered={true}
            confirmLoading={confirmLoadingApprove}
            okButtonProps={{
              style: {
                backgroundColor: "#861fa2",
                color: "white",
                width: "80px",
              },
            }}
            cancelButtonProps={{
              style: {
                borderColor: "#ccc",
                color: "black",
                width: "80px",
              },
            }}
          >
            <p style={{ fontSize: "1rem", margin: "10px 0px 40px 0px" }}>
              Are you sure you want to approve this registration?
            </p>
          </Modal> */}

                    {/* <Modal
            title={
              <p style={{ fontSize: "1.2rem" }}>Decline this registration</p>
            }
            open={openModalConfirmDecline}
            onOk={handleDeclineDocument}
            onCancel={() => setOpenModalConfirmDecline(false)}
            okText="Yes"
            cancelText="No"
            centered={true}
            confirmLoading={confirmLoadingDecline}
            okButtonProps={{
              style: {
                backgroundColor: "#861fa2",
                color: "white",
                width: "80px",
              },
            }}
            cancelButtonProps={{
              style: {
                borderColor: "#ccc",
                color: "black",
                width: "80px",
              },
            }}
          >
            <p style={{ fontSize: "1rem", margin: "10px 0px 40px 0px" }}>
              Are you sure you want to decline this registration?
            </p>
          </Modal> */}
                    <ModalConfirm
                        title='Approve this registration'
                        description='Are you sure you want to approve this registration?'
                        handleConfirm={handleApproveDocument}
                    >
                        <Button
                            size='default'
                            variant='success'
                            type='button'
                            disabled={
                                feedBackFromAccountant.trim() === ""
                                    ? false
                                    : true
                            }
                        >
                            Approve
                        </Button>
                    </ModalConfirm>
                </>
            )}
        </div>
    );
};
