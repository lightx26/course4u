import FilterWrap from './FilterWrap'

export default function Sidebar() {
    return (
        <div className='w-fit'>
            <h3 className='pb-5 ml-8 text-xl font-medium text-neutral-900 w-[20%] grow'>Filter</h3>
            <FilterWrap />
        </div>
    )
}
