import FilterComponent from './FilterComponent'
import { fetchCategories } from '../../API/FakeAPICategory'
import { fetchCourseRatings } from '../../API/FakeAPIRating'
import { fetchCourseLevels } from '../../API/FakeAPICouseLevel'
import { fetchCoursePlatforms } from '../../API/FakeAPIPlatform'

export default function FilterWrap() {

    return (
        <div className='flex flex-col max-h-[80vh] gap-5 pb-12 ml-8 overflow-y-auto min-w-64 custom-scrollbar select-none'>
            <FilterComponent isHaveSearch={true} title='Category' promise={fetchCategories()} />
            <FilterComponent title='Rating' promise={fetchCourseRatings()} />
            <FilterComponent title='Course level' promise={fetchCourseLevels()} />
            <FilterComponent title='Platform' promise={fetchCoursePlatforms()} />
        </div>
    )
}
