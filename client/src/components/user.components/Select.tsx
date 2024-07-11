type OptionType = {
    value: number | string,
    content: string
}

type PropType = {
    listOption: OptionType[],
    value: string,
    onSortByChange: (e: React.ChangeEvent<HTMLSelectElement>) => void
}

export default function Select({ listOption, value, onSortByChange }: PropType) {
    return (
        <div>
            Sort by:
            <select className='p-3 ml-2 text-left transition-all duration-700 bg-transparent border border-gray-300 rounded-sm' value={value} onChange={(event) => { onSortByChange(event) }}>
                {listOption.map((item) => (
                    <option key={item.value} value={item.value}>{item.content}</option>
                ))}
            </select>
        </div>
    )
}
