import React from 'react'

export default function EmptyPage({ content }: { content: string }) {
    return (
        <div className='flex items-center justify-center h-[100%]'>
            <h3 className='p-10 m-16 text-xl font-medium text-center'>{content}</h3>
        </div>
    )
}
