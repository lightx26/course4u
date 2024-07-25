const OrderByMapping = (orderByInput: string): string => {
    switch(orderByInput.toLowerCase()){
        case "created date":
            return "id";
        case "last modified":
            return "lastUpdated";
        default:
            return "id";
    }
}

export default OrderByMapping;