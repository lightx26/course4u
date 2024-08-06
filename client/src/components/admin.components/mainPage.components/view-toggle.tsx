import { Label } from "../../ui/label.tsx"
import {RadioGroup, RadioGroupItem} from "../../ui/radio-group.tsx";

import {setAdminView} from "../../../redux/slice/admin-registration.slice.ts";
import {setAccountantView} from "../../../redux/slice/accountant-registration.slice.ts";
import {useDispatch} from "react-redux";

type PropType = {
    role: string,
    view: string
}

const ViewToggle = ({role, view}: PropType) => {
    const dispatch = useDispatch();

    const handleSetView = (view: string): void => {
        if (role === 'admin'){
            dispatch(setAdminView(view))
        }
        else{
            dispatch(setAccountantView(view))
        }
    }

    return (
        <div className="mb-2">
            <RadioGroup className="flex gap-4" value={view} onValueChange={(value) => handleSetView(value)}>
                <div className="flex items-center space-x-1">
                    <RadioGroupItem value="grid"/>
                    <Label htmlFor="grid">Grid view</Label>
                </div>
                <div className="flex items-center space-x-1">
                    <RadioGroupItem value="list"/>
                    <Label htmlFor="list">List view</Label>
                </div>
            </RadioGroup>
        </div>
    )
}

export default ViewToggle;