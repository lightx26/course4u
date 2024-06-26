import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import Registrations from "../user.components/registrations";

type Props = {
  children: React.ReactNode;
  id?: number;
};
export const RegistrationModal = ({ children, id }: Props) => {
  return (
    <Dialog>
      <DialogTrigger asChild>{children}</DialogTrigger>
      <DialogContent className="p-0 lg:max-w-[1352px] w-full rounded-xl min-h-[783px]">
        <DialogTitle></DialogTitle>
        <Registrations className="border-0 w-full" id={id} />
      </DialogContent>
    </Dialog>
  );
};
