import {AppstoreOutlined, MenuOutlined} from '@ant-design/icons'
import {Space, Switch} from 'antd';

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
        if (role === 'admin') {
            dispatch(setAdminView(view))
        } else {
            dispatch(setAccountantView(view))
        }
    }

    const handleSwitched = () => {
        if (view === 'grid'){
            handleSetView('list')
        }
        else{
            handleSetView('grid')
        }
    }

    const checked = view === 'grid'

    return (
        <Space direction='vertical'>
            <Switch
                value={checked}
                checkedChildren={<AppstoreOutlined/>}
                unCheckedChildren={<MenuOutlined />}
                onChange={handleSwitched}
            />
        </Space>
    )
}

export default ViewToggle;