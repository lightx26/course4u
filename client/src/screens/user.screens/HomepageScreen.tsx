import Sidebar from "../../components/user.components/Homepage/Sidebar";
import MainContent from "../../components/user.components/Homepage/MainContent";

export default function HomepageScreen() {
    return (
        <div className="flex items-start py-8 mx-auto max-w-screen-2xl">
            <Sidebar />
            <MainContent />
        </div>
    )
}
