import React from 'react';
import {Alert, Snackbar} from "@mui/material";

interface SnackbarComponentProps {
    snackbars: { message: string; severity: 'success' | 'error' | 'info' | 'warning'; open: boolean,duration?:number |null ,position?:string | null }[];
    handleCloseSnackbar: (index: number) => void;
}

const SnackbarComponent: React.FC<SnackbarComponentProps> = ({ snackbars, handleCloseSnackbar }) => {
    return (
        <div>
            {snackbars.map((snackbar, index) => (
                <Snackbar
                    key={index}
                    open={snackbar.open}
                    autoHideDuration={snackbar.duration || 99999}
                    onClose={() => handleCloseSnackbar(index)}
                    anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                >
                    <Alert
                        onClose={() => handleCloseSnackbar(index)}
                        severity={snackbar.severity}
                        sx={{ width: '100%' }}
                    >
                        {snackbar.message}
                    </Alert>
                </Snackbar>
            ))}
        </div>
    );
};

export default SnackbarComponent;