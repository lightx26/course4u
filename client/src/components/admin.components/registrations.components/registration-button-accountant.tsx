import { useState } from "react";
import { useRegistrationModal } from "../../../hooks/use-registration-modal";
import { Status } from "../../../utils/index";
import { Button } from "../../ui/button";
import { Modal } from "antd";
import { approve_Decline_Document_Registration } from "../../../apiService/Registration.service";
import { toast } from "sonner";
import { useRefreshState } from "../../../hooks/use-refresh-state";
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
  //approve
  const [openModalConfirmApprove, setOpenModalConfirmApprove] = useState(false);
  const [confirmLoadingApprove, setConfirmLoadingApprove] = useState(false);

  //decline
  const [openModalConfirmDecline, setOpenModalConfirmDecline] = useState(false);
  const [confirmLoadingDecline, setConfirmLoadingDecline] = useState(false);

  const handleApproveDocument = async () => {
    setConfirmLoadingApprove(true);
    const trimmedFeedback = feedBackFromAccountant.trim();
    const response = await approve_Decline_Document_Registration(
      id,
      trimmedFeedback,
      document,
      "VERIFIED"
    );
    if (response && response.status === 200) {
      setConfirmLoadingApprove(false);
      toast.success("Registration approved", {
        style: { color: "green" },
        description: "Registration approved successfully",
      });

      //dispatch action fetch list registration
      setRegistrationFlagAccountant();

      close();
    } else if (response && response.status !== 200 && response.data) {
      setConfirmLoadingApprove(false);
      toast.error("Failed to approve registration", {
        style: { color: "red" },
        description: `${response.data.message}`,
      });
    }
  };
  const handleDeclineDocument = async () => {
    setConfirmLoadingDecline(true);
    const trimmedFeedback = feedBackFromAccountant.trim();
    if (trimmedFeedback === "" || trimmedFeedback === undefined) {
      setConfirmLoadingDecline(false);
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
      setConfirmLoadingDecline(false);
      toast.success("Registration declined", {
        style: { color: "green" },
        description: "Registration declined successfully",
      });

      //dispatch action fetch list registration
      setRegistrationFlagAccountant();

      close();
    } else if (response && response.status !== 200 && response.data) {
      setConfirmLoadingDecline(false);
      toast.error("Failed to declined registration", {
        style: { color: "red" },
        description: `${response.data.message}`,
      });
    }
  };
  return (
    <div className="flex justify-end gap-4">
      {status === Status.VERIFYING && (
        <>
          <Button
            size="lg"
            variant="danger"
            type="button"
            onClick={() => setOpenModalConfirmDecline(true)}
            disabled={feedBackFromAccountant.trim() === "" ? true : false}
          >
            Decline and send feedback
          </Button>
          <Modal
            title="Approve Registration"
            open={openModalConfirmApprove}
            onOk={handleApproveDocument}
            onCancel={() => setOpenModalConfirmApprove(false)}
            okText="OK"
            cancelText="Cancel"
            centered={true}
            confirmLoading={confirmLoadingApprove}
          >
            <p>Are you sure you want to approve this registration?</p>
          </Modal>

          <Modal
            title="Decline Registration"
            open={openModalConfirmDecline}
            onOk={handleDeclineDocument}
            onCancel={() => setOpenModalConfirmDecline(false)}
            okText="OK"
            cancelText="Cancel"
            centered={true}
            confirmLoading={confirmLoadingDecline}
          >
            <p>Are you sure you want to decline this registration?</p>
          </Modal>
          <Button
            size="lg"
            onClick={() => setOpenModalConfirmApprove(true)}
            variant="success"
            type="button"
            disabled={feedBackFromAccountant.trim() === "" ? false : true}
          >
            Approve
          </Button>
        </>
      )}
    </div>
  );
};
