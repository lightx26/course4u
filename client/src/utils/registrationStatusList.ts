type statusProps = {
    value: string,
    content: string
}

const registrationStatusList: statusProps[] = [
    {
        value: "0",
        content: "All",
    },
    {
        value: "1",
        content: "Submitted",
    },
    {
        value: "2",
        content: "Declined",
    },
    {
        value: "3",
        content: "Approved",
    },
    {
        value: "4",
        content: "Done",
    },
    {
        value: "5",
        content: "Verifying",
    },
    {
        value: "6",
        content: "Declined (Document)",
    },
    {
        value: "7",
        content: "Verified",
    },
    {
        value: "8",
        content: "Closed",
    },
    {
        value: "9",
        content: "Discarded",
    },
];

export const registrationStatusListForAccountant = [
  {
    value: "5",
    content: "Verifying",
  },
  {
    value: "6",
    content: "Declined (Document)",
  },
  {
    value: "7",
    content: "Verified",
  },
];

export default registrationStatusList;
