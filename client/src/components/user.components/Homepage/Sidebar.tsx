import FilterWrap from './FilterWrap'

export default function Sidebar() {
    return (
        <div className='w-[20%]'>
            <h3 className='pb-5 ml-8 text-xl font-medium text-neutral-900 grow'>Filter</h3>
            <FilterWrap />
        </div>
    )
}
