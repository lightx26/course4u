import FilterWrap from './FilterWrap'

export default function Sidebar() {
    return (
        <div>
            <h3 className='pb-5 text-xl font-medium text-neutral-900 grow'>Filter by</h3>
            <FilterWrap />
        </div>
    )
}
