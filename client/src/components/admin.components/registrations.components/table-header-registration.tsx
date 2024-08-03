export default function TableHeaderRegistration({ tableHeader }: { tableHeader: string[] }) {
    return (
        <div className="flex items-center py-1 text-base font-bold bg-white rounded-tl-sm rounded-tr-sm">
            <div className="w-[35%] text-center">{tableHeader[0]}</div>
            <div className="w-[20%] text-center">{tableHeader[1]}</div>
            <div className="w-[10%] text-center">{tableHeader[2]}</div>
            <div className="w-[20%] text-center">{tableHeader[3]}</div>
            <div className="w-[15%] text-center">{tableHeader[4]}</div>
        </div>
    )
}
