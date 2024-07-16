type OptionType = {
  value: string;
  content: string;
};

type PropType = {
  readonly listOption: OptionType[];
  readonly value: string;
  readonly onSortByChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
};

export default function Select({
  listOption,
  value,
  onSortByChange,
}: PropType) {
  return (
    <div>
      <select
        className="py-2 text-sm w-full text-left transition-all duration-700 bg-transparent border border-gray-300 bg-white"
        value={value}
        onChange={(event) => {
          onSortByChange(event);
        }}
      >
        {listOption.map((item) => (
          <option key={item.value} value={item.value}>
            {item.content}
          </option>
        ))}
      </select>
    </div>
  );
}
