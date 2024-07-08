export enum Status {
    NONE = "NONE",
    DRAFT = "DRAFT",
    SUBMITTED = "SUBMITTED",
    DECLINED = "DECLINED",
    APPROVED = "APPROVED",
    DONE = "DONE",
    VERIFYING = "VERIFYING",
    DOCUMENT_DECLINED = "DOCUMENT_DECLINED",
    VERIFIED = "VERIFIED",
    CLOSED = "CLOSED",
    DISCARDED = "DISCARDED",
}
export const statusList = [
    {
        value: "ALL",
        content: 'All',
    },
    {
        value: "DRAFT",
        content: 'Draft',
    },
    {
        value: "DISCARDED",
        content: 'Discarded',
    },
    {
        value: "SUBMITTED",
        content: 'Submitted',
    },
    {
        value: "DECLINED",
        content: 'Declined',
    },
    {
        value: "DONE",
        content: 'Done',
    },
    {
        value: "APPROVED",
        content: 'Approved',
    },
    {
        value: "VERIFYING",
        content: 'Verifying',
    },
    {
        value: "VERIFIED",
        content: 'Verified',
    },
    {
        value: "CLOSED",
        content: 'Closed',
    },
    {
        value: "DOCUMENT_DECLINED",
        content: 'Document(Declined)',
    }
];