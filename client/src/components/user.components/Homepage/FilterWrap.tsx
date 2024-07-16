import { useSelector } from 'react-redux';
import FilterComponent from './FilterComponent'
import { FilterItemType } from './MainContent'
import { RootState } from '../../../redux/store/store';

export default function FilterWrap() {
    const listCategory = useSelector((state: RootState) => state.category);
    const mockFilterComponentData: FilterItemType[][] = [[], [], [], []];
    mockFilterComponentData[0] = listCategory.map((item) => {
        return {
            id: `cat${item.id}`,
            name: item.name,
            countNumber: 1200,
            parentID: 'cat1',
            checked: false
        }
    });

    mockFilterComponentData[1] = ([
        {
            id: 'rat1',
            name: '5 Star',
            countNumber: 1200,
            parentID: 'cat2',
            checked: false
        },
        {
            id: 'rat2',
            name: '4 Star & up',
            countNumber: 800,
            parentID: 'cat2',
            checked: false
        },
        {
            id: 'rat3',
            name: '3 Star & up',
            countNumber: 900,
            parentID: 'cat2',
            checked: false
        },
        {
            id: 'rat4',
            name: '2 Star & up',
            countNumber: 1500,
            parentID: 'cat2',
            checked: false
        },
        {
            id: 'rat5',
            name: '1 Star & up',
            countNumber: 700,
            parentID: 'cat2',
            checked: false
        },
    ]
    );
    mockFilterComponentData[2] = ([
        {
            id: 'lev1',
            name: 'Beginner',
            countNumber: 1200,
            parentID: 'cat3',
            checked: false
        },
        {
            id: 'lev2',
            name: 'Intermediate',
            countNumber: 800,
            parentID: 'cat3',
            checked: false
        },
        {
            id: 'lev3',
            name: 'Advanced',
            countNumber: 900,
            parentID: 'cat3',
            checked: false
        },
    ]
    );

    mockFilterComponentData[3] = ([
        {
            id: 'plat1',
            name: 'Udemy',
            countNumber: 1200,
            parentID: 'cat4',
            checked: false
        },
        {
            id: 'plat2',
            name: 'Coursera',
            countNumber: 800,
            parentID: 'cat4',
            checked: false
        },
        {
            id: 'plat3',
            name: 'Linkedin',
            countNumber: 900,
            parentID: 'cat4',
            checked: false
        },
    ]
    );
    return (
        <div className='sticky max-h-[90vh] custom-scrollbar overflow-y-auto border-2 border-gray-100 border-solid p-1 rounded-md w-full'>
            <div className='sticky flex flex-col w-full gap-5 pb-12 select-none min-w-64'>
                <FilterComponent key={'cat1'} isHaveSearch={true} title='Category' list={mockFilterComponentData[0]} />
                <FilterComponent key={'cat2'} isMultipleChoice={false} isHaveSearch={false} title='Rating' list={mockFilterComponentData[1]} />
                <FilterComponent key={'cat3'} isHaveSearch={false} title='Level' list={mockFilterComponentData[2]} />
                <FilterComponent key={'cat4'} isHaveSearch={false} title='Platform' list={mockFilterComponentData[3]} />
            </div>
        </div>
    )
}
