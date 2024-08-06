import {AppstoreOutlined, MenuOutlined} from '@ant-design/icons'
import {ConfigProvider, Flex, Radio} from "antd";

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

    return (
        <ConfigProvider
            theme={{
                components:{
                    Radio: {
                        buttonSolidCheckedActiveBg: "#861FA2",
                        buttonSolidCheckedBg: "#861FA2",
                        buttonSolidCheckedHoverBg: "#861FA2",
                        colorPrimary: "#861FA2"
                    }
                }
            }}
        >
            <Flex vertical gap="middle" className="mr-4">
                <Radio.Group value={view} buttonStyle="solid" size={"large"}
                             onChange={(e) => handleSetView(e.target.value)}>
                    <Radio.Button value="grid"><AppstoreOutlined/></Radio.Button>
                    <Radio.Button value="list"><MenuOutlined/></Radio.Button>
                </Radio.Group>
            </Flex>
        </ConfigProvider>
    )
}

export default ViewToggle;