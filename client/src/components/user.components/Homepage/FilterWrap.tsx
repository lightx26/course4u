import FilterComponent from './FilterComponent'
import { fetchCategories } from '../../../API/FakeAPICategory'
import { fetchCourseRatings } from '../../../API/FakeAPIRating'
import { fetchCourseLevels } from '../../../API/FakeAPICouseLevel'
import { fetchCoursePlatforms } from '../../../API/FakeAPIPlatform'
import { v4 as uuidv4 } from 'uuid';

export default function FilterWrap() {

    return (
        <div className='max-h-[90vh] custom-scrollbar overflow-y-auto border-2 border-gray-100 border-solid p-2 rounded-md w-fit'>
            <div className='flex flex-col gap-5 pb-12 select-none min-w-64 w-fit'>
                <FilterComponent key={uuidv4()} isHaveSearch={true} title='Category' promise={fetchCategories()} />
                <FilterComponent key={uuidv4()} title='Rating' promise={fetchCourseRatings()} />
                <FilterComponent key={uuidv4()} title='Course level' promise={fetchCourseLevels()} />
                <FilterComponent key={uuidv4()} title='Platform' promise={fetchCoursePlatforms()} />
            </div>
        </div>
    )
}
