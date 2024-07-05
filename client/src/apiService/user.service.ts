import instance from "../utils/customizeAxios";

const handleLogin = async (username: string, password: string) => {
  try {
    const response = await instance.post("/auth/login", {
      username,
      password,
    });

    if (response)
      return {
        access_token: response?.data?.access_token,
      };
  } catch (error: any) {
    throw new Error(error.message);
  }
};

const getUserDetails = async (access_token: string) => {
  // try {
  //   const response = await instance.get("https://dummyjson.com/auth/me", {
  //     headers: {
  //       Authorization: `Bearer ${access_token}`,
  //     },
  //   });
  //   return response.data;
  // } catch (error) {
  //   throw new Error(error.message);
  // }
  return {
    id: "ID001",
    Username: "NguyenVanA",
    FullName: "Nguyễn Thúc Hoàng",
    AvatarUrl:
      "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQzgup07l-IhHq_3j2u1iEK9tJWGJGKInZWA&s",
    Email: "hoang.thuc.nguyen@mgm-tp.com",
    Role: "User",
    DateOfBirth: "17/11/2003",
    Gender: "Male",
    Telephone: "0935357982",
  };
};

export { handleLogin, getUserDetails };
