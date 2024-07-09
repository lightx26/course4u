import FilterComponent from './FilterComponent'
import { FilterItemType } from './MainContent'

export default function FilterWrap() {
    const mockFilterComponentData: FilterItemType[][] = [];
    mockFilterComponentData[0] = ([
        {
            id: 'cat1',
            name: 'Web Development',
            countNumber: 1200,
            parentID: 'cat1',
        },
        {
            id: 'cat2',
            name: 'Front-end Development',
            countNumber: 800,
            parentID: 'cat1',
        },
        {
            id: 'cat3',
            name: 'Back-end Development',
            countNumber: 900,
            parentID: 'cat1',
        },
        {
            id: 'cat4',
            name: 'Full Stack Development',
            countNumber: 1500,
            parentID: 'cat1',
        },
        {
            id: 'cat5',
            name: 'Mobile Development',
            countNumber: 700,
            parentID: 'cat1',
        },
        {
            id: 'cat6',
            name: 'Data Science',
            countNumber: 600,
            parentID: 'cat1',
        },
        {
            id: 'cat7',
            name: 'Machine Learning',
            countNumber: 500,
            parentID: 'cat1',
        },
        {
            id: 'cat8',
            name: 'Cybersecurity',
            countNumber: 400,
            parentID: 'cat1',
        },
        {
            id: 'cat9',
            name: 'Cloud Computing',
            countNumber: 300,
            parentID: 'cat1',
        },
        {
            id: 'cat10',
            name: 'Blockchain',
            countNumber: 200,
            parentID: 'cat1',
        },
        // Thêm nhiều phân loại khác nếu cần
    ]
    );
    mockFilterComponentData[1] = ([
        {
            id: 'rat1',
            name: '5 Star',
            countNumber: 1200,
            parentID: 'cat2',
        },
        {
            id: 'rat2',
            name: '4 Star & up',
            countNumber: 800,
            parentID: 'cat2',
        },
        {
            id: 'rat3',
            name: '3 Star & up',
            countNumber: 900,
            parentID: 'cat2',
        },
        {
            id: 'rat4',
            name: '2 Star & up',
            countNumber: 1500,
            parentID: 'cat2',
        },
        {
            id: 'rat5',
            name: '1 Star & up',
            countNumber: 700,
            parentID: 'cat2',
        },
    ]
    );
    mockFilterComponentData[2] = ([
        {
            id: 'lev1',
            name: 'Beginner',
            countNumber: 1200,
            parentID: 'cat3',
        },
        {
            id: 'lev2',
            name: 'Intermediate',
            countNumber: 800,
            parentID: 'cat3',
        },
        {
            id: 'lev3',
            name: 'Advanced',
            countNumber: 900,
            parentID: 'cat3',
        },
    ]
    );

    mockFilterComponentData[3] = ([
        {
            id: 'plat1',
            name: 'Udemy',
            countNumber: 1200,
            parentID: 'cat4',
        },
        {
            id: 'plat2',
            name: 'Coursera',
            countNumber: 800,
            parentID: 'cat4',
        },
        {
            id: 'plat3',
            name: 'Linkedin Learning',
            countNumber: 900,
            parentID: 'cat4',
        },
    ]
    );
    return (
        <div className='sticky max-h-[90vh] custom-scrollbar overflow-y-auto border-2 border-gray-100 border-solid p-2 rounded-md w-full'>
            <div className='sticky flex flex-col w-full gap-5 pb-12 select-none min-w-64'>
                <FilterComponent key={'cat1'} isHaveSearch={true} title='Category' list={mockFilterComponentData[0]} />
                <FilterComponent key={'cat2'} isHaveSearch={false} title='Rating' list={mockFilterComponentData[1]} />
                <FilterComponent key={'cat3'} isHaveSearch={false} title='Level' list={mockFilterComponentData[2]} />
                <FilterComponent key={'cat4'} isHaveSearch={false} title='Platform' list={mockFilterComponentData[3]} />
            </div>
        </div>
    )
}
