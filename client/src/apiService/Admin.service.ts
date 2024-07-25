import instance from "../utils/customizeAxios";
import {RegistrationParamsType} from "../redux/slice/adminRegistration.slice.ts";
import OrderByMapping from "../utils/orderByMapping.ts";

async function fetchAllRegistrations(params: RegistrationParamsType, page: number = 1) {
    const status: string = params.status == "Declined (Document)"
        ? "DOCUMENT_DECLINED"
        : params.status.toUpperCase();

    const orderBy: string = OrderByMapping(params.orderBy);

    const search: string = params.search.toLowerCase();
    const isAscending: string = params.isAscending ? 'true' : 'false';

    const url = `/admin/registrations?status=${status}&search=${search}&orderBy=${orderBy}&isAscending=${isAscending}&page=${page}`

    try {

        const response = await instance.get(url);
        return response.data;
    } catch (error) {
        throw new Error("Error while fetching registrations");
    }
}

export {fetchAllRegistrations};
