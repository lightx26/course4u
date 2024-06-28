import FilterComponent from './FilterComponent'
import { fetchCategories } from '../../../API/FakeAPICategory'
import { fetchCourseRatings } from '../../../API/FakeAPIRating'
import { fetchCourseLevels } from '../../../API/FakeAPICouseLevel'
import { fetchCoursePlatforms } from '../../../API/FakeAPIPlatform'

export default function FilterWrap() {

    return (
        <div className='max-h-[90vh] custom-scrollbar overflow-y-auto border-2 border-gray-100 border-solid p-2 rounded-md'>
            <div className='flex flex-col gap-5 pb-12 select-none min-w-64'>
                <FilterComponent isHaveSearch={true} title='Category' promise={fetchCategories()} />
                <FilterComponent title='Rating' promise={fetchCourseRatings()} />
                <FilterComponent title='Course level' promise={fetchCourseLevels()} />
                <FilterComponent title='Platform' promise={fetchCoursePlatforms()} />
            </div>
        </div>
    )
}
