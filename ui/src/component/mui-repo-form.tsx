import React, {useState} from "react";
import {styled} from '@mui/material/styles';
import {Box, Button, Grid2, Paper, TextField} from "@mui/material";

const Item = styled(Paper)(({theme}) => ({
    backgroundColor: '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
    ...theme.applyStyles('dark', {
        backgroundColor: '#1A2027',
    }),
}));

interface MuiRepoFormProps {
    handleSubmitForm: (data: { url: string } | null) => void;
}

const MuiRepoForm: React.FC<MuiRepoFormProps> = ({handleSubmitForm}) => {
    const [formData, setFormData] = useState<{ url: string } | null>({url:''});
    const [formErrors, setFormErrors] = useState<Array<{ field: string, message: string }> | null>(null);
    const  handleSubmit = (event: React.SyntheticEvent) => {
        var errors: Array<{ field: string, message: string }> | null = null;

        event.preventDefault();

        errors = (validateForm(formData));

        if (isValidForm(errors)) {
            handleSubmitForm(formData);
            setFormErrors(null);
            setFormData({url:""});

        } else {
            setFormErrors(errors);
        }
    }

    const  validateForm = (formData: { url: string } | null) => {

        var errors: any[] = [];

        if (!formData?.url) {
            errors = [...errors, {field: "url", message: "URL is required."}];
        } else if (!validateUrl(formData.url)) {
            errors = [...errors, {field: "url", message: "Invalid URL."}];
        }

       return errors;
    }

    const validateUrl = (input: string) => {
        // URL validation regex (simple version)
        const regex = /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/i;
        return regex.test(input);
    };

    const isValidForm = (errors: Array<{ field: string, message: string }> | null) => {

        return errors?.length === 0;
    }

    const handleChangeUrl = (value: string) => {
        setFormData({
            url: value
        });
    }
    return (
        <Box sx={{flexGrow: 1}}>
            <form>
                <Grid2 container spacing={2}>
                    <Grid2 size={12}>
                        <Item>
                            <TextField
                                name={"url"}
                                label={"URL"}
                                required={true}
                                fullWidth={true}
                                value={formData?.url}
                                error={formErrors?.some(e => e.field === "url")}
                                helperText={formErrors?.find(e => e.field === "url")?.message}
                                onChange={(e)=>handleChangeUrl(e.target.value)}
                            />
                        </Item>
                        <Item>
                            <Button variant={"contained"}
                                    onClick={(event) => {
                                        handleSubmit(event as React.SyntheticEvent);
                                    }}
                            >
                                Add New
                            </Button>
                            <Button variant={"contained"}
                                    type={"reset"}
                            >
                               Clear
                            </Button>
                        </Item>
                    </Grid2>

                </Grid2>
            </form>
        </Box>


    )
}
export default MuiRepoForm;