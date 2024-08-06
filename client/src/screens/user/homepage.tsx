import Sidebar from "../../components/course/filter/sidebar";
import MainContent from "../../components/user/main-content";

export default function HomepageScreen() {
    return (
        <div className='flex items-start gap-1 px-3 py-8 mx-auto max-w-screen-2xl min-h-screen'>
            <Sidebar />
            <MainContent />
        </div>
    );
}
